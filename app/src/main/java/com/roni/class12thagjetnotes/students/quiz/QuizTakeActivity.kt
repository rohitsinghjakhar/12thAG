package com.roni.class12thagjetnotes.students.quiz

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ActivityQuizTakeBinding
import com.roni.class12thagjetnotes.students.models.Question
import com.roni.class12thagjetnotes.students.models.QuizResult
import com.roni.class12thagjetnotes.students.viewmodels.StudentDeskViewModel
import kotlinx.coroutines.launch

class QuizTakeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizTakeBinding
    private lateinit var viewModel: StudentDeskViewModel

    private var quizId: String = ""
    private var quizTitle: String = ""
    private var timeLimit: Int = 0
    private var passingScore: Int = 50

    private var questions = listOf<Question>()
    private var currentQuestionIndex = 0
    private var userAnswers = mutableMapOf<String, Int>()
    private var startTime: Long = 0
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizTakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizId = intent.getStringExtra("quiz_id") ?: ""
        quizTitle = intent.getStringExtra("quiz_title") ?: "Quiz"
        timeLimit = intent.getIntExtra("time_limit", 0)
        passingScore = intent.getIntExtra("passing_score", 50)

        if (quizId.isEmpty()) {
            Toast.makeText(this, "Invalid quiz", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel = ViewModelProvider(this)[StudentDeskViewModel::class.java]

        setupToolbar()
        setupButtons()
        observeViewModel()

        startTime = System.currentTimeMillis()
        viewModel.loadQuestions(quizId)
    }

    private fun setupToolbar() {
        binding.toolbar.title = quizTitle
        binding.toolbar.setNavigationOnClickListener {
            showExitConfirmation()
        }
    }

    private fun setupButtons() {
        binding.nextButton.setOnClickListener {
            saveCurrentAnswer()
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                displayQuestion()
            }
        }

        binding.previousButton.setOnClickListener {
            saveCurrentAnswer()
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                displayQuestion()
            }
        }

        binding.submitButton.setOnClickListener {
            showSubmitConfirmation()
        }
    }

    private fun observeViewModel() {
        viewModel.questions.observe(this) { questionsList ->
            if (questionsList.isNotEmpty()) {
                questions = questionsList
                displayQuestion()
                startTimer()
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "No questions found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startTimer() {
        if (timeLimit > 0) {
            binding.timerLayout.visibility = View.VISIBLE
            val totalMillis = timeLimit * 60 * 1000L

            timer = object : CountDownTimer(totalMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val minutes = millisUntilFinished / 1000 / 60
                    val seconds = (millisUntilFinished / 1000) % 60
                    binding.timerText.text = String.format("%02d:%02d", minutes, seconds)

                    // Change color when time is running out
                    if (minutes < 2) {
                        binding.timerText.setTextColor(ContextCompat.getColor(this@QuizTakeActivity, R.color.error))
                    }
                }

                override fun onFinish() {
                    binding.timerText.text = "00:00"
                    Toast.makeText(this@QuizTakeActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                    submitQuiz()
                }
            }.start()
        } else {
            binding.timerLayout.visibility = View.GONE
        }
    }

    private fun displayQuestion() {
        if (currentQuestionIndex >= questions.size) return

        val question = questions[currentQuestionIndex]

        // Update UI
        binding.questionNumber.text = "Question ${currentQuestionIndex + 1}/${questions.size}"
        binding.questionText.text = question.questionText

        // Set options
        binding.option1.text = question.option1
        binding.option2.text = question.option2
        binding.option3.text = question.option3
        binding.option4.text = question.option4

        // Clear previous selection
        binding.optionsGroup.clearCheck()

        // Restore previous answer if exists
        userAnswers[question.id]?.let { answer ->
            when (answer) {
                1 -> binding.option1.isChecked = true
                2 -> binding.option2.isChecked = true
                3 -> binding.option3.isChecked = true
                4 -> binding.option4.isChecked = true
            }
        }

        // Update button visibility
        binding.previousButton.visibility = if (currentQuestionIndex > 0) View.VISIBLE else View.GONE
        binding.nextButton.visibility = if (currentQuestionIndex < questions.size - 1) View.VISIBLE else View.GONE
        binding.submitButton.visibility = if (currentQuestionIndex == questions.size - 1) View.VISIBLE else View.GONE

        // Update progress
        val progress = ((currentQuestionIndex + 1).toFloat() / questions.size * 100).toInt()
        binding.progressBar.progress = progress
        binding.progressText.text = "$progress% Complete"
    }

    private fun saveCurrentAnswer() {
        if (currentQuestionIndex >= questions.size) return

        val question = questions[currentQuestionIndex]
        val selectedId = binding.optionsGroup.checkedRadioButtonId

        if (selectedId != -1) {
            val selectedButton = findViewById<RadioButton>(selectedId)
            val answer = when (selectedButton) {
                binding.option1 -> 1
                binding.option2 -> 2
                binding.option3 -> 3
                binding.option4 -> 4
                else -> 0
            }
            if (answer > 0) {
                userAnswers[question.id] = answer
            }
        }
    }

    private fun showSubmitConfirmation() {
        val unanswered = questions.size - userAnswers.size
        val message = if (unanswered > 0) {
            "You have $unanswered unanswered questions. Do you want to submit anyway?"
        } else {
            "Are you sure you want to submit the quiz?"
        }

        AlertDialog.Builder(this)
            .setTitle("Submit Quiz")
            .setMessage(message)
            .setPositiveButton("Submit") { _, _ ->
                saveCurrentAnswer()
                submitQuiz()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showExitConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Exit Quiz")
            .setMessage("Are you sure you want to exit? Your progress will be lost.")
            .setPositiveButton("Exit") { _, _ ->
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun submitQuiz() {
        timer?.cancel()

        // Calculate results
        var correctAnswers = 0
        questions.forEach { question ->
            val userAnswer = userAnswers[question.id] ?: 0
            if (userAnswer == question.correctAnswer) {
                correctAnswers++
            }
        }

        val totalQuestions = questions.size
        val score = (correctAnswers.toFloat() / totalQuestions * 100).toInt()
        val timeTaken = (System.currentTimeMillis() - startTime) / 1000 // in seconds
        val passed = score >= passingScore

        // Save result
        val result = QuizResult(
            quizId = quizId,
            userId = "user_id", // Replace with actual user ID from auth
            userName = "Student", // Replace with actual user name
            score = score,
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            wrongAnswers = totalQuestions - correctAnswers,
            timeTaken = timeTaken,
            answers = userAnswers,
            completedAt = System.currentTimeMillis(),
            passed = passed
        )

        lifecycleScope.launch {
            val saved = viewModel.saveQuizResult(result)
            if (saved) {
                navigateToResult(result)
            } else {
                Toast.makeText(this@QuizTakeActivity, "Failed to save result", Toast.LENGTH_SHORT).show()
                navigateToResult(result)
            }
        }
    }

    private fun navigateToResult(result: QuizResult) {
        val intent = Intent(this, QuizResultActivity::class.java).apply {
            putExtra("quiz_title", quizTitle)
            putExtra("score", result.score)
            putExtra("correct_answers", result.correctAnswers)
            putExtra("total_questions", result.totalQuestions)
            putExtra("time_taken", result.timeTaken)
            putExtra("passing_score", passingScore)
            putExtra("passed", result.passed)
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        showExitConfirmation()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}