package com.roni.class12thagjetnotes.students.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.databinding.ItemQuizBinding
import com.roni.class12thagjetnotes.students.models.Quiz

class QuizAdapter(
    private val onQuizClick: (Quiz) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    private var quizzes = listOf<Quiz>()

    fun submitList(list: List<Quiz>) {
        quizzes = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizzes[position])
    }

    override fun getItemCount() = quizzes.size

    inner class QuizViewHolder(
        private val binding: ItemQuizBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quiz: Quiz) {
            binding.quizTitle.text = quiz.title
            binding.quizDescription.text = quiz.description
            binding.quizQuestionCount.text = "${quiz.totalQuestions} Questions"

            if (quiz.timeLimit > 0) {
                binding.quizTimeLimit.text = "${quiz.timeLimit} minutes"
            } else {
                binding.quizTimeLimit.text = "No time limit"
            }

            binding.root.setOnClickListener {
                binding.cardView.animate()
                    .scaleX(0.98f)
                    .scaleY(0.98f)
                    .setDuration(100)
                    .withEndAction {
                        binding.cardView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                        onQuizClick(quiz)
                    }
                    .start()
            }
        }
    }
}