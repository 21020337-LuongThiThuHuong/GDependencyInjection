package com.example.gandroidrestapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel : ViewModel() {

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> get() = _pokemonList

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val pokemonApi = retrofit.create(PokemonApi::class.java)

    fun fetchPokemonData(limit: Int, offset: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val call = pokemonApi.getPokemonList(limit, offset)
                call.enqueue(object : Callback<PokemonResponse> {
                    override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                        if (response.isSuccessful) {
                            _pokemonList.postValue(response.body()?.results)
                        } else {
                        }
                    }

                    override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
