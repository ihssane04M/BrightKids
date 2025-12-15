package com.example.brightkids

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brightkids.learning.databinding.ItemLetterBinding
import com.example.brightkids.model.Letter

class LetterAdapter(
    private val letters: List<Letter>,
    private val onLetterClick: (Letter) -> Unit
) : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    // Color cycle: red, blue, green, yellow, purple
    // Background colors (lighter shades)
    private val backgroundColors = listOf(
        Color.parseColor("#FFE57373"), // letter_red
        Color.parseColor("#FF81D4FA"), // letter_blue
        Color.parseColor("#FFA5D6A7"), // letter_green
        Color.parseColor("#FFFFF59D"), // letter_yellow
        Color.parseColor("#FFCE93D8")  // letter_purple
    )
    
    // Text colors (darker shades)
    private val textColors = listOf(
        Color.parseColor("#FFC62828"), // letter_text_red
        Color.parseColor("#FF0277BD"), // letter_text_blue
        Color.parseColor("#FF2E7D32"), // letter_text_green
        Color.parseColor("#FFE65100"), // letter_text_orange
        Color.parseColor("#FF7B1FA2")  // letter_text_purple
    )

    inner class LetterViewHolder(private val binding: ItemLetterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(letter: Letter, position: Int) {
            binding.tvLetter.text = letter.letter
            
            // Apply cyclic colors
            val colorIndex = position % backgroundColors.size
            val bgColor = backgroundColors[colorIndex]
            val txtColor = textColors[colorIndex]
            
            // binding.root is the MaterialCardView
            val cardView = binding.root as com.google.android.material.card.MaterialCardView
            cardView.setCardBackgroundColor(bgColor)
            binding.tvLetter.setTextColor(txtColor)
            
            binding.root.setOnClickListener {
                onLetterClick(letter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val binding = ItemLetterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LetterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        holder.bind(letters[position], position)
    }

    override fun getItemCount() = letters.size
}