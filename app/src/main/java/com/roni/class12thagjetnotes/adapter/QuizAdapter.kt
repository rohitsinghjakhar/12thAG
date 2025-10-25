package com.roni.class12thagjetnotes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.models.quiz.Quiz

class QuizAdapter(
    private val quizList: List<Quiz>,
    private val onItemClick: (Quiz) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardQuiz)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvQuizTitle)
        private val tvSubject: TextView = itemView.findViewById(R.id.tvQuizSubject)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvQuizDescription)
        private val tvQuestions: TextView = itemView.findViewById(R.id.tvQuestions)
        private val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        private val tvMarks: TextView = itemView.findViewById(R.id.tvMarks)
        private val tvDifficulty: TextView = itemView.findViewById(R.id.tvDifficulty)
        private val tvAttempts: TextView = itemView.findViewById(R.id.tvAttempts)
        private val btnStart: Button = itemView.findViewById(R.id.btnStartQuiz)

        fun bind(quiz: Quiz) {
            tvTitle.text = quiz.title
            tvSubject.text = quiz.subject
            tvDescription.text = quiz.description
            tvQuestions.text = "${quiz.totalQuestions} Questions"
            tvDuration.text = "${quiz.duration} min"
            tvMarks.text = "${quiz.maxMarks} marks"
            tvDifficulty.text = quiz.difficulty
            tvAttempts.text = "${quiz.attempts} attempts"

            // Set difficulty color
            val difficultyColor = when (quiz.difficulty.lowercase()) {
                "easy" -> Color.parseColor("#4CAF50")
                "medium" -> Color.parseColor("#FF9800")
                "hard" -> Color.parseColor("#F44336")
                else -> Color.parseColor("#607D8B")
            }
            tvDifficulty.setTextColor(difficultyColor)

            btnStart.setOnClickListener {
                onItemClick(quiz)
            }

            cardView.setOnClickListener {
                onItemClick(quiz)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizList[position])
    }

    override fun getItemCount() = quizList.size
}