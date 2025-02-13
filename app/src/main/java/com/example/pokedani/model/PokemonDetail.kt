package com.example.pokedani.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val base_experience: Int,
    val abilities: List<Ability>,
    val forms: List<NamedAPIResource>,
    val location_area_encounters: String,
    val moves: List<NamedAPIResource>,
    val species: NamedAPIResource,
    val stats: List<Stat>,
    val types: List<TypeSlot>,
    val sprites: Sprites,
    val items: List<Item>,
    val cries: Cry
)

data class Ability(
    val name: String,
    val url: String,
    val is_hidden: Boolean,
    val slot: Int
)

data class TypeSlot(
    val slot: Int,
    val type: NamedAPIResource
)

data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: NamedAPIResource
)

data class Sprites(
    val front_default: String,
    val back_default: String
)

data class Item(
    val name: String,
    val url: String
)

data class Cry(
    val latest: String,
    val legacy: String
)

data class NamedAPIResource(
    val name: String,
    val url: String
)