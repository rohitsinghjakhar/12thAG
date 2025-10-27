package com.roni.class12thagjetnotes.students.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ActivityQuizResultBinding

class QuizResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quizTitle = intent.getStringExtra("quiz_title") ?: "Quiz"
        val score = intent.getIntExtra("score", 0)
        val correctAnswers = intent.getIntExtra("correct_answers", 0)
        val totalQuestions = intent.getIntExtra("total_questions", 0)
        val timeTaken = intent.getLongExtra("time_taken", 0)
        val passingScore = intent.getIntExtra("passing_score", 50)
        val passed = intent.getBooleanExtra("passed", false)

        setupToolbar(quizTitle)
        displayResults(score, correctAnswers, totalQuestions, timeTaken, passingScore, passed)
        setupButtons()
    }

    private fun setupToolbar(quizTitle: String) {
        binding.toolbar.title = quizTitle
        binding.toolbar.subtitle = "Quiz Result"
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun displayResults(
        score: Int,
        correctAnswers: Int,
        totalQuestions: Int,
        timeTaken: Long,
        passingScore: Int,
        passed: Boolean
    ) {
        // Display score
        binding.scoreText.text = "$score%"
        binding.scoreProgress.progress = score

        // Set result status
        if (passed) {
            binding.resultStatus.text = "Passed! 🎉"
            binding.resultStatus.setTextColor(ContextCompat.getColor(this, R.color.success))
            binding.scoreProgress.progressTintList = ContextCompat.getColorStateList(this, R.color.success)
            binding.resultIcon.setImageResource(R.drawable.ic_success)
            binding.resultIcon.setColorFilter(ContextCompat.getColor(this, R.color.success))
        } else {
            binding.resultStatus.text = "Failed"
            binding.resultStatus.setTextColor(ContextCompat.getColor(this, R.color.error))
            binding.scoreProgress.progressTintList = ContextCompat.getColorStateList(this, R.color.error)
            binding.resultIcon.setImageResource(R.drawable.ic_error)
            binding.resultIcon.setColorFilter(ContextCompat.getColor(this, R.color.error))
        }

        // Display statistics
        binding.correctAnswersText.text = "$correctAnswers"
        binding.wrongAnswersText.text = "${totalQuestions - correctAnswers}"
        binding.totalQuestionsText.text = "$totalQuestions"

        // Format and display time taken
        val minutes = timeTaken / 60
        val seconds = timeTaken % 60
        binding.timeTakenText.text = String.format("%d:%02d", minutes, seconds)

        // Display passing score
        binding.passingScoreText.text = "$passingScore%"

        // Display performance message
        binding.performanceMessage.text = getPerformanceMessage(score, passed)
    }

    private fun getPerformanceMessage(score: Int, passed: Boolean): String {
        return when {
            score >= 90 -> "Excellent! You've mastered this topic! ⭐"
            score >= 75 -> "Great job! You have a strong understanding of the material."
            score >= 60 -> "Good work! Keep practicing to improve further."
            passed -> "You passed! Review the topics you missed to strengthen your knowledge."
            else -> "Don't worry! Review the material and try again. You've got this!"
        }
    }

    private fun setupButtons() {
        binding.homeButton.setOnClickListener {
            // Navigate to home/main activity
            finishAffinity() // Close all activities in the stack
        }

        binding.retryButton.setOnClickListener {
            finish() // Go back to quiz list
        }

        binding.reviewButton.setOnClickListener {
            // TODO: Implement review answers functionality
            // This would show correct vs incorrect answers
            // You can create a ReviewAnswersActivity for this
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}