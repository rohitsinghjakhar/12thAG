package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.jet.models.JetQuiz
import kotlinx.android.synthetic.main.item_jet_quiz.view.*

class JetQuizAdapter(
    private val quizList: List<JetQuiz>,
    private val onQuizClick: (JetQuiz) -> Unit
) : RecyclerView.Adapter<JetQuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardQuiz: CardView = itemView.cardQuiz
        val tvTitle: TextView = itemView.tvQuizTitle
        val tvSubject: TextView = itemView.tvQuizSubject
        val tvTopic: TextView = itemView.tvQuizTopic
        val tvQuestions: TextView = itemView.tvQuizQuestions
        val tvDuration: TextView = itemView.tvQuizDuration
        val tvDifficulty: TextView = itemView.tvQuizDifficulty
        val tvAttempts: TextView = itemView.tvQuizAttempts
        val tvAvgScore: TextView = itemView.tvQuizAvgScore
        val tvPremiumBadge: TextView = itemView.tvPremiumBadge
        val tvLiveBadge: TextView = itemView.tvLiveBadge

        fun bind(quiz: JetQuiz) {
            tvTitle.text = quiz.title
            tvSubject.text = quiz.subject
            tvTopic.text = quiz.topic
            tvQuestions.text = "${quiz.totalQuestions} Qs"
            tvDuration.text = "${quiz.duration} min"
            tvAttempts.text = "${quiz.attemptCount} attempts"

            // Average score
            val avgScore = if (quiz.attemptCount > 0) {
                String.format("%.1f%%", quiz.averageScore)
            } else {
                "N/A"
            }
            tvAvgScore.text = avgScore

            // Difficulty badge
            tvDifficulty.text = quiz.difficulty
            val difficultyColor = when (quiz.difficulty) {
                "Easy" -> R.color.success
                "Medium" -> R.color.warning
                "Hard" -> R.color.error
                else -> R.color.info
            }
            tvDifficulty.setBackgroundColor(
                itemView.context.resources.getColor(difficultyColor, null)
            )

            // Show/hide badges
            tvPremiumBadge.visibility = if (quiz.isPremium) View.VISIBLE else View.GONE
            tvLiveBadge.visibility = if (quiz.isLive) View.VISIBLE else View.GONE

            // Card color based on difficulty
            val cardColor = when (quiz.difficulty) {
                "Easy" -> R.color.card_2
                "Medium" -> R.color.card_3
                "Hard" -> R.color.card_1
                else -> R.color.card_4
            }
            cardQuiz.setCardBackgroundColor(
                itemView.context.resources.getColor(cardColor, null)
            )

            // Click listener
            cardQuiz.setOnClickListener {
                onQuizClick(quiz)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizList[position])
    }

    override fun getItemCount(): Int = quizList.size
}