package com.example.gandroidrestapi

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter

    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        viewModel.pokemonList.observe(this) { pokemonList ->
            pokemonAdapter = PokemonAdapter(pokemonList)
            recyclerView.adapter = pokemonAdapter
        }

        // Define limit and offset values
        val limit = 20 // example limit for the number of Pokemon to fetch
        val offset = 0 // example offset for pagination

        // Initiate network request with the limit and offset parameters
        viewModel.fetchPokemonData(limit, offset)
    }
}
