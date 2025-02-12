package com.example.pokedani.model

// Modelo principal del Pokémon
data class PokemonDetail(
    val id: Int,                         // ID del Pokémon
    val name: String,                    // Nombre del Pokémon
    val height: Int,                     // Altura del Pokémon
    val weight: Int,                     // Peso del Pokémon
    val base_experience: Int,            // Experiencia base del Pokémon
    val abilities: List<Ability>,        // Lista de habilidades del Pokémon
    val forms: List<NamedAPIResource>,   // Formas del Pokémon
    val location_area_encounters: String, // URL de los encuentros del área
    val moves: List<NamedAPIResource>,   // Movimientos del Pokémon
    val species: NamedAPIResource,       // Especie del Pokémon
    val stats: List<Stat>,               // Estadísticas base
    val types: List<TypeSlot>,           // Tipos del Pokémon
    val sprites: Sprites,                // URLs de las imágenes del Pokémon
    val items: List<Item>,               // Objetos relacionados
    val cries: Cry                       // URL de los sonidos del Pokémon
)

// Modelo para las habilidades
data class Ability(
    val name: String,
    val url: String,
    val is_hidden: Boolean,
    val slot: Int
)

// Modelo para los tipos del Pokémon
data class TypeSlot(
    val slot: Int,
    val type: NamedAPIResource
)

// Modelo para las estadísticas
data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: NamedAPIResource
)

// Modelo para las imágenes
data class Sprites(
    val front_default: String,
    val back_default: String
)

// Modelo para objetos relacionados
data class Item(
    val name: String,
    val url: String
)

// Modelo para los sonidos
data class Cry(
    val latest: String,
    val legacy: String
)

// Modelo genérico para recursos nombrados (reutilizable)
data class NamedAPIResource(
    val name: String,
    val url: String
)
