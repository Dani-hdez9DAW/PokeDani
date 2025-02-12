package com.example.pokedani.service

import com.example.pokedani.model.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers(
        "Authorization: Bearer a13bfad9c4535d7b15a803c8383e3a8c",
        "Content-Type: application/json"
    )
    @GET("pokemon")
    suspend fun getPokemons(): PokemonResponse
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://pokeapi.co/api/v2/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService: ApiService = retrofit.create(ApiService::class.java)

object PokemonRepository {
    // Función suspend para obtener la lista de Pokémon utilizando el apiService
    suspend fun fetchPokemons(): PokemonResponse {
        return apiService.getPokemons()
    }
}
