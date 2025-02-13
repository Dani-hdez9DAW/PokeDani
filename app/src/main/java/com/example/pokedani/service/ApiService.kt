package com.example.pokedani.service

import com.example.pokedani.model.PokemonDetail
import com.example.pokedani.model.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

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
    suspend fun getPokemonDetail(@retrofit2.http.Path("name") name: String): PokemonDetail
}

object PokemonRepository {
    // Función suspend para obtener la lista de Pokémon utilizando el apiService
    suspend fun fetchPokemons(): PokemonResponse {
        return apiService.getPokemons()
    }
}
object PokemonDetailRepository {
    suspend fun fetchPokemons(): List<Unit> {
        val response = apiService.getPokemons()
        // Realiza una solicitud adicional para cada Pokémon y obtén los detalles
        return response.results.map { pokemon ->
            apiService.getPokemonDetail(pokemon.name)
        }
    }
}

