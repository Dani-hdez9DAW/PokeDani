package com.example.pokedani.repository

import com.example.pokedani.model.PokemonDetail
import com.example.pokedani.model.PokemonResponse
import com.example.pokedani.service.apiService

object PokemonRepository {
    // Función suspend para obtener la lista de Pokémon utilizando el apiService
    suspend fun fetchPokemons(): PokemonResponse {
        return apiService.getPokemons()
    }

    // Función suspend para obtener los detalles de un Pokémon utilizando el apiService
    suspend fun fetchPokemonDetail(name: String): PokemonDetail {
        return apiService.getPokemonDetail(name)
    }
}