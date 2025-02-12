package com.example.pokedani.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokedani.model.PokemonDetail

@Composable
fun PokemonItem(pokemon: PokemonDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Nombre del Pokémon
            Text(text = pokemon.name, style = MaterialTheme.typography.titleLarge)

            // Tipos
            Row {
                pokemon.types.forEach { typeSlot ->
                    Text(
                        text = typeSlot.type.name,
                        modifier = Modifier
                            .padding(4.dp)
                            .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Estadísticas y Habilidades
            Text(text = "Altura: ${pokemon.height} decímetros")
            Text(text = "Peso: ${pokemon.weight} hectogramos")
            Text(text = "Experiencia base: ${pokemon.base_experience}")
            Text(text = "Habilidades:")
            pokemon.abilities.forEach { ability ->
                Text(
                    text = "- ${ability.name} (Oculta: ${ability.is_hidden})"
                )
            }
        }
    }
}