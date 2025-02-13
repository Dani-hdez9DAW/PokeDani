package com.example.pokedani.view

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pokedani.R
import com.example.pokedani.model.PokemonDetail
import com.example.pokedani.repository.PokemonRepository
import java.util.Locale

val typeColors = mapOf(
    "normal" to Color(0xFFA8A77A),
    "fire" to Color(0xFFEE8130),
    "water" to Color(0xFF6390F0),
    "electric" to Color(0xFFF7D02C),
    "grass" to Color(0xFF7AC74C),
    "ice" to Color(0xFF96D9D6),
    "fighting" to Color(0xFFC22E28),
    "poison" to Color(0xFFA33EA1),
    "ground" to Color(0xFFE2BF65),
    "flying" to Color(0xFFA98FF3),
    "psychic" to Color(0xFFF95587),
    "bug" to Color(0xFFA6B91A),
    "rock" to Color(0xFFB6A136),
    "ghost" to Color(0xFF735797),
    "dragon" to Color(0xFF6F35FC),
    "dark" to Color(0xFF705746),
    "steel" to Color(0xFFB7B7CE),
    "fairy" to Color(0xFFD685AD)
)

fun getStatColor(stat: Int): Color {
    return when {
        stat <= 30 -> Color.Red
        stat in 31..65 -> Color(0xFFFFA500)
        stat in 66..85 -> Color.Yellow
        else -> Color.Green
    }
}

@Composable
fun PokemonTypeButton(type: String) {
    val backgroundColor = typeColors[type.lowercase(Locale.ROOT)] ?: Color.LightGray
    Box(
        modifier = Modifier
            .padding(3.dp)
            .background(backgroundColor)
            .border(BorderStroke(2.dp, Color.Black))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun PokemonItem(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: return
    var pokemonDetail by remember { mutableStateOf<PokemonDetail?>(null) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.pokedanimusic) }

    DisposableEffect(Unit) {
        mediaPlayer.start()
        onDispose {
            mediaPlayer.release()
        }
    }
    LaunchedEffect(pokemonName) {
        loading = true
        try {
            val response = PokemonRepository.fetchPokemonDetail(pokemonName)
            pokemonDetail = response
        } catch (e: Exception) {
            errorMessage = e.message ?: "Error desconocido"
        } finally {
            loading = false
        }
    }

    val backgroundColor = pokemonDetail?.types?.firstOrNull()?.type?.name?.let { typeName ->
        typeColors[typeName] ?: Color.White
    } ?: Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color.Gray,
                                        shape = CircleShape
                                    )
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = "${pokemonDetail!!.id}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(getPokemonImageUrl(pokemonDetail!!.id))
                                        .apply { crossfade(true) }
                                        .build()
                                ),
                                contentDescription = "${pokemonDetail!!.name} image",
                                modifier = Modifier
                                    .size(220.dp)
                                    .padding(bottom = 16.dp)
                            )
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pokemonDetail!!.name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                                },
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    letterSpacing = 1.5.sp,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            pokemonDetail!!.types.forEach { type ->
                                PokemonTypeButton(type.type.name)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = Color.Black)
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Características",
                                fontSize = 15.sp,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        HorizontalDivider(thickness = 1.dp, color = Color.Black)

                        Text(
                            text = "Altura: ${pokemonDetail!!.height / 10.0} m",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Peso: ${pokemonDetail!!.weight / 10.0} kg",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Exp Base: ${pokemonDetail!!.base_experience}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        HorizontalDivider(thickness = 1.dp, color = Color.Black)
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Estadísticas",
                                fontSize = 15.sp,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        HorizontalDivider(thickness = 1.dp, color = Color.Black)
                        pokemonDetail!!.stats.forEach { stat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stat.stat.name.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                                    },
                                    modifier = Modifier.width(80.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.blue_gray_700),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = stat.base_stat.toString(),
                                    modifier = Modifier.width(40.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(20.dp)
                                        .background(Color.Gray)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(fraction = stat.base_stat / 255f)
                                            .height(20.dp)
                                            .background(getStatColor(stat.base_stat))
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}


fun getPokemonImageUrl(numberId: Int?): String {
    val numberId = numberId ?: 0
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$numberId.png"
}
