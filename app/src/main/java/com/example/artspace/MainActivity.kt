
package com.example.artspace


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme

//Classe de données pour représenter un véhicule dans la galerie

data class Artwork(
    val id: Int,
    val imageRes: Int,
    val title: String,
    val artist: String,
    val year: String,
    val description: String? = null
)

//Activité principale de l'application Car Gallery

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceApp()
                }
            }
        }
    }
}

//Composable principal de l'application Car Gallery
@Composable
fun ArtSpaceApp() {
    // Collection de véhicules pour la galerie - Liste statique des 4 voitures
    val artworks = listOf(
        Artwork(
            id = 1,
            imageRes = R.drawable.gls_600, 
            title = "Mercedes-Maybach GLS",
            artist = "Mercedes-Benz",
            year = "2024",
            description = "SUV de luxe ultime combinant élégance et performance dans un design imposant"
        ),
        Artwork(
            id = 2,
            imageRes = R.drawable.gtr_35,
            title = "Nissan GT-R",
            artist = "Nissan",
            year = "2024",
            description = "Supercar japonaise légendaire, symbole de performance et d'innovation technologique"
        ),
        Artwork(
            id = 3,
            imageRes = R.drawable.urus_performante2,
            title = "Lamborghini Urus",
            artist = "Lamborghini",
            year = "2024",
            description = "Le super SUV italien qui redéfinit les codes du luxe sportif automobile"
        ),
        Artwork(
            id = 4,
            imageRes = R.drawable.lexus_rx_350,
            title = "Lexus NX",
            artist = "Lexus",
            year = "2024",
            description = "SUV compact premium alliant raffinement japonais et design contemporain"
        )
    )

    // États pour gérer la navigation et l'affichage de l'interface
    var currentArtworkIndex by remember { mutableStateOf(0) } // Index du véhicule actuellement affiché (0-3)
    var showDescription by remember { mutableStateOf(false) } // Contrôle l'affichage/masquage de la description

    // Véhicule actuellement sélectionné basé sur l'index
    val currentArtwork = artworks[currentArtworkIndex]

    // Structure principale de l'interface - Disposition verticale
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titre principal de l'application
        Text(
            text = "Car Gallery",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Section d'affichage du véhicule - Prend l'espace disponible
        ArtworkWall(
            artwork = currentArtwork, // Passe le véhicule actuel
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section d'informations sur le véhicule
        ArtworkDescriptor(
            artwork = currentArtwork,
            showDescription = showDescription,
            onToggleDescription = { showDescription = !showDescription }, // Fonction pour basculer l'affichage
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section des boutons de navigation
        DisplayController(
            // Fonction pour aller au véhicule précédent
            onPreviousClick = {
                currentArtworkIndex = when (currentArtworkIndex) {
                    0 -> artworks.size - 1
                    else -> currentArtworkIndex - 1
                }
                showDescription = false // Masquer la description lors du changement
            },
            // Fonction pour aller au véhicule suivant
            onNextClick = {
                currentArtworkIndex = when (currentArtworkIndex) {
                    artworks.size - 1 -> 0 // Si on est au dernier, aller au premier (navigation circulaire)
                    else -> currentArtworkIndex + 1 // Sinon, incrémenter l'index
                }
                showDescription = false // Masquer la description lors du changement
            },
            currentPosition = currentArtworkIndex + 1, // Position actuelle (1-4) pour l'affichage
            totalArtworks = artworks.size // Nombre total de véhicules (4)
        )
    }
}

//Composable qui affiche l'image du véhicule dans un cadre élégant

@Composable
fun ArtworkWall(
    artwork: Artwork,
    modifier: Modifier = Modifier
) {
    // Surface avec ombre portée pour simuler un cadre accroché au mur
    Surface(
        modifier = modifier
            .shadow(
                elevation = 8.dp, // Hauteur de l'ombre de 8dp
                shape = RoundedCornerShape(12.dp) // Coins arrondis de 12dp
            ),
        shape = RoundedCornerShape(12.dp), // Forme de la surface avec coins arrondis
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = artwork.imageRes),
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = Color.Gray.copy(alpha = 0.3f), // Couleur grise semi-transparente
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Fit // Ajuste l'image sans déformation
            )
        }
    }
}

//Composable qui affiche les informations du véhicule
@Composable
fun ArtworkDescriptor(
    artwork: Artwork,
    showDescription: Boolean,
    onToggleDescription: () -> Unit,
    modifier: Modifier
) {
    // Surface avec ombre légère pour séparer visuellement les informations
    Surface(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        // Organisation verticale des informations
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Titre du véhicule (nom/modèle)
            Text(
                text = artwork.title,
                fontSize = 20.sp, //
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Marque et année du véhicule
            Text(
                text = "${artwork.artist} (${artwork.year})", // Format: "Marque (Année)"
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Affichage du bouton et de la description si elle existe
            if (artwork.description != null) {
                Spacer(modifier = Modifier.height(8.dp))

                // Bouton pour afficher/masquer la description
                TextButton(onClick = onToggleDescription) {
                    Text(
                        text = if (showDescription) "Masquer la description" else "Voir la description",
                        fontSize = 12.sp
                    )
                }

                // Affichage conditionnel de la description
                if (showDescription) {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Texte de description dans un conteneur avec fond
                    Text(
                        text = artwork.description,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

//Composable pour les contrôles de navigation

@Composable
fun DisplayController(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    currentPosition: Int,
    totalArtworks: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), // Remplit toute la largeur
            horizontalArrangement = Arrangement.SpaceEvenly // Espacement égal entre les boutons
        ) {
            // Bouton "Précédent"
            Button(
                onClick = onPreviousClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp) // Bouton avec coins très arrondis
            ) {
                Text(
                    text = "Précédent",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Bouton "Suivant"
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Suivant",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

//Aperçu pour le mode téléphone en portrait
@Preview(showBackground = true)
@Composable
fun ArtSpaceAppPreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}

//Aperçu pour le mode tablette en paysage
@Preview(
    showBackground = true,
    widthDp = 800, // Largeur de tablette
    heightDp = 480, // Hauteur de tablette
    name = "Tablette Paysage"
)
@Composable
fun ArtSpaceAppTabletPreview() {
    ArtSpaceTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Car Gallery",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ArtworkWall(
                artwork = Artwork(
                    id = 1,
                    imageRes = R.drawable.gls_600,
                    title = "Mercedes-Maybach GLS",
                    artist = "Mercedes-Benz",
                    year = "2024",
                    description = "SUV de luxe ultime combinant élégance et performance dans un design imposant"
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ArtworkDescriptor(
                artwork = Artwork(
                    id = 1,
                    imageRes = R.drawable.gls_600,
                    title = "Mercedes-Maybach GLS",
                    artist = "Mercedes-Benz",
                    year = "2024",
                    description = "SUV de luxe ultime combinant élégance et performance dans un design imposant"
                ),
                showDescription = false, // Description masquée par défaut dans l'aperçu
                onToggleDescription = { }, // Fonction vide pour l'aperçu
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Boutons en bas - Espace fixe
            DisplayController(
                onPreviousClick = { },
                onNextClick = { },
                currentPosition = 1,
                totalArtworks = 4 // Total fixe pour l'aperçu
            )
        }
    }
}