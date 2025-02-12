package com.example.pokedani.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pokedani.model.Pokemon
import com.example.pokedani.service.PokemonRepository
@Composable
fun PokemonScreen(navController: NavHostController) {
    var modifier = Modifier
    // Estado que almacena la lista de Pokémon
    var pokemons by remember { mutableStateOf<List<Pokemon>>(emptyList()) }
    // Estado que indica si se está cargando la información
    var loading by remember { mutableStateOf(false) }
    // Estado para almacenar un posible mensaje de error
    var errorMessage by remember { mutableStateOf("") }

    // Lanzamos la petición al iniciarse la pantalla
    // LaunchedEffect se ejecuta una única vez al iniciar el Composable
    LaunchedEffect(Unit) {
        loading = true // Indicamos que se inicia la carga
        try {
            // Llamada a la función del Repository para obtener la lista de Pokémon
            val response = PokemonRepository.fetchPokemons()
            pokemons = response.results
        } catch (e: Exception) {
            // En caso de error, se almacena el mensaje de error
            errorMessage = e.message ?: "Error desconocido"
        } finally {
            // Finaliza la carga
            loading = false
        }
    }

    // Estructura principal de la pantalla: una columna que ocupa todo el tamaño disponible
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Se evalúan los estados para mostrar el contenido adecuado
        when {
            loading -> {
                // Mientras se carga la información, se muestra un indicador de progreso centrado
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            // Si hay un error, se muestra el mensaje de error
            errorMessage.isNotEmpty() -> {
                Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            }
            else -> {
                // Cuando se han obtenido los datos, se muestra la lista de Pokémon utilizando LazyColumn
                LazyColumn {
                    // Para cada Pokémon en la lista se invoca el Composable PokemonItem
                    items(pokemons) { pokemon ->
                        PokemonItem(pokemon)
                    }
                }
            }
        }
    }
}

/**
 * PokemonItem: Composable que muestra la información de un Pokémon.
 * Se muestra dentro de una Card con un Row que contiene el nombre del Pokémon.
 */
fun getPokemonImageUrl(pokemonUrl: String): String {
    // La URL contiene algo como "https://pokeapi.co/api/v2/pokemon/{id}/"
    val pokemonId = pokemonUrl.split("/").filter { it.isNotEmpty() }.last() // Extrae el ID
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
}

@Composable
fun PokemonItem(pokemon: Pokemon) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del Pokémon (Coil)
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(getPokemonImageUrl(pokemon.url))
                        .apply { crossfade(true) }
                        .build()
                ),
                contentDescription = "${pokemon.name} image",
                modifier = Modifier
                    .size(64.dp) // Tamaño de la imagen
                    .padding(end = 16.dp)
            )

            // Nombre del Pokémon
            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}