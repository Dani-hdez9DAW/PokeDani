package com.example.pokedani.service

import com.example.pokedani.model.PokemonDetail
import com.example.pokedani.model.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://pokeapi.co/api/v2/") // URL base solo con el dominio
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService: ApiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @Headers(
        "Authorization: Bearer a13bfad9c4535d7b15a803c8383e3a8c",
        "Content-Type: application/json"
    )
    @GET("pokemon?limit=100000&offset=0") // El resto de la ruta va aquí
    suspend fun getPokemons(): PokemonResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDetail
}

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
