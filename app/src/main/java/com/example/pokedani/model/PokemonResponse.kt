package com.example.pokedani.model

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)


