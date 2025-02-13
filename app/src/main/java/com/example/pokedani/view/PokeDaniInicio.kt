package com.example.pokedani.view

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedani.R
import com.example.pokedani.ui.theme.PokeDaniTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class PokeDaniInicio : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "main") {
                composable("main") { MainScreen(navController) }
                composable("menuPokemon") {
                    PokemonScreen(navController)
                }
                composable("PokemonItem/{pokemonName}") { backStackEntry ->
                    PokemonItem(navController, backStackEntry)
                }
            }
        }
    }

    @Composable
    fun MainScreen(navController: NavHostController) {
        val context = LocalContext.current
        val backgroundColor = Color(0xFFFFD700)
        var isLoading by remember { mutableStateOf(false) }
        val mediaPlayer = remember { MediaPlayer.create(context, R.raw.maininiciopokedani) }

        DisposableEffect(Unit) {
            mediaPlayer.start()
            onDispose {
                mediaPlayer.release()
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pokedanilogo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.white),
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                        LaunchedEffect(Unit) {
                            val delayTime = Random.nextLong(2000, 5000)
                            delay(delayTime)
                            navController.navigate("menuPokemon")
                        }
                    } else {
                        Button(
                            onClick = {
                                isLoading = true
                            },
                            colors = ButtonDefaults.buttonColors((colorResource(id = R.color.amber_200))), // Color teal para complementar el fondo amarillo
                            modifier = Modifier
                                .padding(bottom = 32.dp)
                                .size(width = 200.dp, height = 60.dp)
                        ) {
                            Text(
                                text = "Comenzar",
                                color = colorResource(id = R.color.deep_orange_700),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        PokeDaniTheme {
            val navController = rememberNavController()
            Scaffold {
                MainScreen(navController)
            }
        }
    }
}