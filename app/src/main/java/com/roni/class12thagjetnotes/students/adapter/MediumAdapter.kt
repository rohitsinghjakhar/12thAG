package com.roni.class12thagjetnotes.students.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemMediumBinding
import com.roni.class12thagjetnotes.students.models.Medium

class MediumAdapter(
    private val onMediumClick: (Medium) -> Unit
) : RecyclerView.Adapter<MediumAdapter.MediumViewHolder>() {

    private var mediums = listOf<Medium>()

    fun submitList(list: List<Medium>) {
        mediums = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediumViewHolder {
        val binding = ItemMediumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MediumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediumViewHolder, position: Int) {
        holder.bind(mediums[position])
    }

    override fun getItemCount() = mediums.size

    inner class MediumViewHolder(
        private val binding: ItemMediumBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(medium: Medium) {
            binding.mediumName.text = medium.name

            when (medium.name.lowercase()) {
                "hindi" -> {
                    binding.mediumIcon.setImageResource(R.drawable.ic_hindi)
                    binding.mediumBackground.setBackgroundResource(R.drawable.gradient_hindi)
                }
                "english" -> {
                    binding.mediumIcon.setImageResource(R.drawable.ic_hindi)
                    binding.mediumBackground.setBackgroundResource(R.drawable.gradient_english_medium)
                }
                else -> {
                    binding.mediumIcon.setImageResource(R.drawable.ic_language)
                    binding.mediumBackground.setBackgroundResource(R.drawable.gradient_default)
                }
            }

            binding.root.setOnClickListener {
                binding.cardView.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction {
                        binding.cardView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                        onMediumClick(medium)
                    }
                    .start()
            }
        }
    }
}
