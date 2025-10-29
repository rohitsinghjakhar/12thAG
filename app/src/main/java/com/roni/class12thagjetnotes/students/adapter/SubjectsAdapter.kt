package com.roni.class12thagjetnotes.students.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemSubjectBinding
import com.roni.class12thagjetnotes.students.models.Subject

class SubjectsAdapter(
    private val onSubjectClick: (Subject) -> Unit
) : RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder>() {

    private var subjects = listOf<Subject>()

    fun submitList(list: List<Subject>) {
        subjects = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = ItemSubjectBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(subjects[position])
    }

    override fun getItemCount() = subjects.size

    inner class SubjectViewHolder(
        private val binding: ItemSubjectBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject) {
            binding.subjectName.text = subject.name

            val context = binding.root.context

            // ✅ Load icon from Firebase URL safely
            Glide.with(context)
                .load(subject.icon) // now this is a valid HTTPS URL
                .placeholder(R.drawable.ic_default_subject)
                .error(R.drawable.ic_default_subject)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.subjectIcon)

            // ✅ Optional gradient background selection
            val backgroundRes = getSubjectBackground(subject.name)
            binding.subjectBackground.setBackgroundResource(backgroundRes)

            // ✅ Click animation + navigation
            binding.root.setOnClickListener {
                Log.d("SubjectClick", "Clicked subject: ${subject.id}")
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
                        onSubjectClick(subject)
                    }
                    .start()
            }
        }

        // 🔹 Background gradients for visual variety
        private fun getSubjectBackground(subjectName: String): Int {
            return when (subjectName.lowercase()) {
                "mathematics", "maths" -> R.drawable.gradient_maths
                "science" -> R.drawable.gradient_science
                "english" -> R.drawable.gradient_english
                "computer science", "computers", "it" -> R.drawable.gradient_computer
                "economics" -> R.drawable.gradient_economics
                "political science" -> R.drawable.gradient_political_science
                else -> R.drawable.gradient_default_subject
            }
        }
    }
}
