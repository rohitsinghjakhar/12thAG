package com.roni.class12thagjetnotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemQuizBinding
import com.roni.class12thagjetnotes.models.firebase.JetContent
import com.roni.class12thagjetnotes.models.quiz.Quiz

class QuizListAdapter(
    private val onQuizClick: (Quiz) -> Unit
) : ListAdapter<Quiz, QuizListAdapter.QuizViewHolder>(QuizDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = getItem(position)
        holder.bind(quiz)
    }

    inner class QuizViewHolder(private val binding: ItemQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quiz: Quiz) {
            binding.quizTitle.text = quiz.title
            binding.quizDescription.text = quiz.description
            binding.quizQuestions.text = "${quiz.totalQuestions} Questions"
            binding.quizDuration.text = "${quiz.timeLimit} mins"
            binding.quizDifficulty.text = quiz.difficulty

            binding.root.setOnClickListener {
                onQuizClick(quiz)
            }
        }
    }

    class QuizDiffCallback : DiffUtil.ItemCallback<Quiz>() {
        override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
            return oldItem == newItem
        }
    }
}

// JetContentAdapter.kt
package com.roni.class12thagjetnotes.adapters

import com.roni.class12thagjetnotes.databinding.ItemJetContentBinding
import com.roni.class12thagjetnotes.models.JetContent

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