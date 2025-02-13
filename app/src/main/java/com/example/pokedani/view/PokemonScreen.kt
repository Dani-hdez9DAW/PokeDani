package com.example.pokedani.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pokedani.model.Pokemon
import com.example.pokedani.service.PokemonRepository
import java.util.Locale

@Composable
fun PokemonScreen(navController: NavHostController) {
    var modifier = Modifier
    var pokemons by remember { mutableStateOf<List<Pokemon>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        loading = true
        try {
            val response = PokemonRepository.fetchPokemons()
            pokemons = response.results
        } catch (e: Exception) {
            errorMessage = e.message ?: "Error desconocido"
        } finally {
            loading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFD700))
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(text = "Buscar Pokémon") },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Buscar")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        when {
            loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage.isNotEmpty() -> {
                Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            }
            else -> {
                val filteredPokemons = pokemons.filter { it.name.contains(searchQuery.text, ignoreCase = true) }
                LazyColumn {
                    items(filteredPokemons) { pokemon ->
                        PokemonItem(navController, pokemon)
                    }
                }
            }
        }
    }
}

fun getPokemonImageUrl(pokemonUrl: String): String {
    val pokemonId = pokemonUrl.split("/").filter { it.isNotEmpty() }.last()
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
}

@Composable
fun PokemonItem(navController: NavHostController, pokemon: Pokemon) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { navController.navigate("PokemonItem/${pokemon.name}") },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB0E0E6)) // Color Aqua suave
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(getPokemonImageUrl(pokemon.url))
                        .apply { crossfade(true) }
                        .build()
                ),
                contentDescription = "${pokemon.name} image",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )

            Text(
                text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
