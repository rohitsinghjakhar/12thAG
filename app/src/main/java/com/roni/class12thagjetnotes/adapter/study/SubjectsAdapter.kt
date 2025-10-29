package com.roni.class12thagjetnotes.adapter.study

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemSubjectBinding
import com.roni.class12thagjetnotes.models.firebase.Subject

class SubjectsAdapter(
    private val onItemClick: (Subject) -> Unit
) : RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder>() {

    private var subjects: List<Subject> = emptyList()

    fun submitList(list: List<Subject>) {
        subjects = list
        notifyDataSetChanged()
    }

    inner class SubjectViewHolder(private val binding: ItemSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject, position: Int) {
            binding.apply {
                // Subject name
                subjectName.text = subject.name

                // Set proper icon for each subject
                subjectIcon.setImageResource(getSubjectIcon(subject.name))

                // Set gradient background based on subject
                subjectBackground.setBackgroundResource(getSubjectBackground(subject.name))

                // Click listener
                root.setOnClickListener { onItemClick(subject) }

                // Item animation
                val animation = AnimationUtils.loadAnimation(root.context, R.anim.item_slide_up)
                animation.startOffset = (position * 100).toLong()
                root.startAnimation(animation)
            }
        }

        // Maps subject names to image icons
        private fun getSubjectIcon(subjectName: String): Int {
            return when (subjectName.lowercase()) {
                "mathematics", "maths" -> R.drawable.ic_maths
                "science" -> R.drawable.ic_science
                "english" -> R.drawable.ic_english
                "history" -> R.drawable.ic_history
                "geography" -> R.drawable.ic_geography
                "physics" -> R.drawable.ic_physics
                "chemistry" -> R.drawable.ic_chemistry
                "biology" -> R.drawable.ic_biology
                "computer" -> R.drawable.ic_computer
                "economics" -> R.drawable.ic_economics
                "political science" -> R.drawable.ic_political_science
                "hindi" -> R.drawable.ic_hindi
                else -> R.drawable.ic_default_subject // fallback image
            }
        }

        // Maps subjects to gradient backgrounds
        private fun getSubjectBackground(subjectName: String): Int {
            return when (subjectName.lowercase()) {
                "mathematics", "maths" -> R.drawable.gradient_maths
                "science" -> R.drawable.gradient_science
                "english" -> R.drawable.gradient_english
                "history" -> R.drawable.gradient_history
                "geography" -> R.drawable.gradient_geography
                "physics" -> R.drawable.gradient_physics
                "chemistry" -> R.drawable.gradient_chemistry
                "biology" -> R.drawable.gradient_biology
                "computer" -> R.drawable.gradient_computer
                "economics" -> R.drawable.gradient_economics
                "political science" -> R.drawable.gradient_political_science
                "hindi" -> R.drawable.gradient_hindi
                else -> R.drawable.gradient_default_subject
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = ItemSubjectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(subjects[position], position)
    }

    override fun getItemCount() = subjects.size
}
