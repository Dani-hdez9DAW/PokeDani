package com.example.pokedani.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pokedani.model.PokemonDetail
import com.example.pokedani.service.PokemonRepository
import java.util.Locale

@Composable
fun PokemonItem(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    // Obtenemos el nombre del Pokémon desde los argumentos de navegación
    val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: return

    // Hacemos la llamada al repositorio para obtener los detalles del Pokémon
    var pokemonDetail by remember { mutableStateOf<PokemonDetail?>(null) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(pokemonName) {
        loading = true
        try {
            // Llamada al repositorio para obtener los detalles del Pokémon
            val response = PokemonRepository.fetchPokemonDetail(pokemonName)
            pokemonDetail = response
        } catch (e: Exception) {
            errorMessage = e.message ?: "Error desconocido"
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage.isNotEmpty() -> {
                Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            }
            pokemonDetail != null -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Imagen del Pokémon
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(getPokemonImageUrl(pokemonDetail!!.sprites.front_default))
                                .apply { crossfade(true) }
                                .build()
                        ),
                        contentDescription = "${pokemonDetail!!.name} image",
                        modifier = Modifier
                            .size(128.dp)
                            .padding(bottom = 16.dp)
                    )

                    // Nombre del Pokémon
                    Text(
                        text = pokemonDetail!!.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Información adicional del Pokémon (puedes personalizar esto según la información disponible)
                    Text(
                        text = "Detalles adicionales sobre ${pokemonDetail!!.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

fun getPokemonImageUrl(imageUrl: String?): String {
    return imageUrl ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"
}
