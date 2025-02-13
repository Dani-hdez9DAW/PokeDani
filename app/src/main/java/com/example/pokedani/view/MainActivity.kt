package com.example.pokedani.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

class MainActivity : ComponentActivity(){
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeDaniTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("menuPokemon") { PokemonScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    val backgroundColor = Color(0xFFF5E1C3)
    var isLoading by remember { mutableStateOf(false) }

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
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.teal_700)),
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                            .size(width = 200.dp, height = 60.dp)
                    ) {
                        Text(
                            text = "Comenzar",
                            color = colorResource(id = R.color.black),
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
    PokeDaniTheme  {
        val navController = rememberNavController()
        Scaffold {
            MainScreen(navController)
        }
    }
}

