package com.example.gandroidrestapi

data class Pokemon(
    val name: String,
    val url: String,
) {
    val id: Int
        get() {
            // Extract ID from the URL, e.g., "https://pokeapi.co/api/v2/pokemon/1/"
            return url
                .split("/".toRegex())
                .dropLast(1)
                .last()
                .toInt()
        }
}
