package com.example.gandroidrestapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {
    @GET("pokemon")
    fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Call<PokemonResponse>
}

data class PokemonResponse(
    val results: List<Pokemon>,
)
