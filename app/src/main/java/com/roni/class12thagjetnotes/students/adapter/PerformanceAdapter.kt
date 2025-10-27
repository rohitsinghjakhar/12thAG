package com.roni.class12thagjetnotes.students.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemPerformanceBinding
import com.roni.class12thagjetnotes.students.models.QuizResult
import java.text.SimpleDateFormat
import java.util.*

class PerformanceAdapter(
    private val onResultClick: (QuizResult) -> Unit
) : RecyclerView.Adapter<PerformanceAdapter.PerformanceViewHolder>() {

    private var results = listOf<QuizResult>()

    fun submitList(list: List<QuizResult>) {
        results = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformanceViewHolder {
        val binding = ItemPerformanceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PerformanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerformanceViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount() = results.size

    inner class PerformanceViewHolder(
        private val binding: ItemPerformanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: QuizResult) {
            // Quiz title (you might want to fetch this from Firestore)
            binding.quizTitle.text = "Quiz Result #${adapterPosition + 1}"

            // Date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            binding.quizDate.text = dateFormat.format(Date(result.completedAt))

            // Score
            binding.scoreText.text = "${result.score}%"
            binding.scoreProgress.progress = result.score

            // Status
            if (result.passed) {
                binding.statusText.text = "Passed"
                binding.statusText.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.success)
                )
                binding.scoreProgress.progressTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.success)
                binding.statusIcon.setImageResource(R.drawable.ic_check_circle)
                binding.statusIcon.setColorFilter(
                    ContextCompat.getColor(binding.root.context, R.color.success)
                )
            } else {
                binding.statusText.text = "Failed"
                binding.statusText.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.error)
                )
                binding.scoreProgress.progressTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.error)
                binding.statusIcon.setImageResource(R.drawable.ic_cancel)
                binding.statusIcon.setColorFilter(
                    ContextCompat.getColor(binding.root.context, R.color.error)
                )
            }

            // Questions info
            binding.questionsInfo.text =
                "${result.correctAnswers}/${result.totalQuestions} correct"

            // Time taken
            val minutes = result.timeTaken / 60
            val seconds = result.timeTaken % 60
            binding.timeTaken.text = String.format("%d:%02d", minutes, seconds)

            binding.root.setOnClickListener {
                onResultClick(result)
            }
        }
    }
}