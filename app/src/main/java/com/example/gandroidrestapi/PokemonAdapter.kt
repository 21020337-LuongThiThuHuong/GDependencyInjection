package com.example.gandroidrestapi

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

class PokemonAdapter(
    private val context: Context,
    private val pokemonList: List<Pokemon>,
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    private val backgroundColors = listOf("#F0A8A8", "#F0D28A", "#BDDBDE", "#B0DFF6", "#C9CCEB")
    private val cacheLimit = 6 // Number of images to cache

    class PokemonViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val pokemonName = itemView.findViewById<TextView>(R.id.pokemonName)
        val pokemonImage = itemView.findViewById<ImageView>(R.id.pokemonImage)
        val pokemonNumber = itemView.findViewById<TextView>(R.id.pokemonNumber)
        val cardLayout = itemView.findViewById<View>(R.id.cardLayout)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PokemonViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return PokemonViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: PokemonViewHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {
        val pokemon = pokemonList[position]
        holder.pokemonName.text =
            pokemon.name.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(
                        Locale.getDefault(),
                    )
                } else {
                    it.toString()
                }
            }

        val formattedId = String.format("#%03d", pokemon.id)
        holder.pokemonNumber.text = formattedId

        // Load the Pokémon image
        val imageFile = File(context.cacheDir, "pokemon_${pokemon.id}.png")
        if (imageFile.exists()) {
            // Use cached image if available
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            holder.pokemonImage.setImageBitmap(bitmap)
        } else {
            // Download and cache image if not available
            val imageUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
            Picasso
                .get()
                .load(imageUrl)
                .into(
                    holder.pokemonImage,
                    object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            if (position < cacheLimit) {
                                holder.pokemonImage.drawable?.let { drawable ->
                                    val bitmap =
                                        (drawable as android.graphics.drawable.BitmapDrawable).bitmap
                                    saveImageToCache(imageFile, bitmap)
                                }
                            }
                        }

                        override fun onError(e: Exception?) {
                            // Handle error
                        }
                    },
                )
        }

        val randomColor = backgroundColors.random()
        holder.cardLayout.setBackgroundColor(android.graphics.Color.parseColor(randomColor))
    }

    override fun getItemCount() = pokemonList.size

    private fun saveImageToCache(
        file: File,
        bitmap: Bitmap,
    ) {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }
}
