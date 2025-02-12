package com.example.pokedani.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokedani.model.Pokemon
import com.example.pokedani.service.PokemonRepository
import com.example.pokedani.ui.theme.PokeDaniTheme

class MainActivity : ComponentActivity(){
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeDaniTheme {
                // Usamos Scaffold para estructurar la pantalla con un TopAppBar
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Pokémon disponibles") })
                    }
                ) { paddingValues ->
                    PokemonScreen(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}

/**
 *
 * PokemonScreen: Pantalla que muestra la lista de Pokémon.
 * Realiza la petición a la API y muestra un indicador de carga o error si es necesario.
 *
 */

@Composable
fun PokemonScreen(modifier: Modifier = Modifier) {
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
@Composable
fun PokemonItem(pokemon: Pokemon) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = pokemon.name)
        }
    }
}
