package com.roni.class12thagjetnotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.models.firebase.JetContent

class JetContentAdapter(
    private val onContentClick: (JetContent) -> Unit
) : ListAdapter<JetContent, JetContentAdapter.JetContentViewHolder>(JetContentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JetContentViewHolder {
        val binding = ItemJetContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JetContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JetContentViewHolder, position: Int) {
        val content = getItem(position)
        holder.bind(content)
    }

    inner class JetContentViewHolder(private val binding: ItemJetContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: JetContent) {
            binding.contentTitle.text = content.title
            binding.contentDescription.text = content.description

            when (content.type) {
                "syllabus" -> binding.contentIcon.setImageResource(R.drawable.ic_syllabus)
                "mock_test" -> binding.contentIcon.setImageResource(R.drawable.ic_quiz)
                "important_dates" -> binding.contentIcon.setImageResource(R.drawable.ic_calendar)
                "preparation_tips" -> binding.contentIcon.setImageResource(R.drawable.ic_tips)
                "study_material" -> binding.contentIcon.setImageResource(R.drawable.ic_books)
            }

            binding.root.setOnClickListener {
                onContentClick(content)
            }
        }
    }

    class JetContentDiffCallback : DiffUtil.ItemCallback<JetContent>() {
        override fun areItemsTheSame(oldItem: JetContent, newItem: JetContent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JetContent, newItem: JetContent): Boolean {
            return oldItem == newItem
        }
    }
}