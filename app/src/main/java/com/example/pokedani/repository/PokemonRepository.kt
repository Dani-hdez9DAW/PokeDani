package com.example.pokedani.repository

import com.example.pokedani.model.PokemonDetail
import com.example.pokedani.model.PokemonResponse
import com.example.pokedani.service.apiService

object PokemonRepository {
    suspend fun fetchPokemons(): PokemonResponse {
        return apiService.getPokemons()
    }

    suspend fun fetchPokemonDetail(name: String): PokemonDetail {
        return apiService.getPokemonDetail(name)
    }
}