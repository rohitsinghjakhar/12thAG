package com.roni.class12thagjetnotes.adapter.study

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemSubjectBinding
import com.roni.class12thagjetnotes.models.firebase.Subject

class SubjectsAdapter(
    private val subjects: List<Subject>,
    private val onItemClick: (Subject) -> Unit
) : RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(private val binding: ItemSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: Subject, position: Int) {
            binding.apply {
                tvSubjectName.text = subject.name
                tvSubjectIcon.text = subject.icon
                tvDescription.text = subject.description

                // Set background color
                try {
                    cardSubject.setCardBackgroundColor(Color.parseColor(subject.color))
                } catch (e: Exception) {
                    cardSubject.setCardBackgroundColor(Color.parseColor("#2196F3"))
                }

                // Click listener
                root.setOnClickListener {
                    onItemClick(subject)
                }

                // Animation
                val animation = AnimationUtils.loadAnimation(root.context, R.anim.item_slide_up)
                animation.startOffset = (position * 100).toLong()
                root.startAnimation(animation)
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