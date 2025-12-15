package com.example.brightkids

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brightkids.learning.R
import com.google.android.material.card.MaterialCardView

class AvatarAdapter(
    private val avatars: List<String>,
    var selectedPosition: Int,
    private val onAvatarSelected: (Int) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    inner class AvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardAvatar)
        private val textEmoji: TextView = itemView.findViewById(R.id.textAvatarEmoji)

        fun bind(avatar: String, position: Int) {
            textEmoji.text = avatar
            
            val isSelected = position == selectedPosition
            if (isSelected) {
                cardView.setCardBackgroundColor(itemView.context.getColor(R.color.yellow_400))
                cardView.strokeWidth = 0
                cardView.strokeColor = itemView.context.getColor(R.color.yellow_400)
                cardView.cardElevation = 12f
                cardView.alpha = 1f
            } else {
                cardView.setCardBackgroundColor(itemView.context.getColor(R.color.gray_100))
                cardView.strokeWidth = 0
                cardView.strokeColor = itemView.context.getColor(R.color.gray_100)
                cardView.cardElevation = 2f
                cardView.alpha = 1f
            }

            cardView.setOnClickListener {
                if (selectedPosition != position) {
                    val oldPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(oldPosition)
                    notifyItemChanged(position)
                    onAvatarSelected(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_avatar, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.bind(avatars[position], position)
    }

    override fun getItemCount(): Int = avatars.size
}

