package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.jet.models.JetQuiz
import kotlinx.android.synthetic.main.item_jet_quiz.view.* // You need to create item_jet_quiz.xml

class JetQuizAdapter(
    private val quizList: List<JetQuiz>,
    private val onClick: (JetQuiz) -> Unit
) : RecyclerView.Adapter<JetQuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(quiz: JetQuiz) {
            itemView.tvQuizTitle.text = quiz.title
            itemView.tvQuizInfo.text = "${quiz.totalQuestions} Questions | ${quiz.duration} Mins"
            itemView.tvQuizTopic.text = quiz.topic

            if (quiz.isPremium) {
                itemView.ivPremiumIcon.visibility = View.VISIBLE
            } else {
                itemView.ivPremiumIcon.visibility = View.GONE
            }

            itemView.setOnClickListener { onClick(quiz) }
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