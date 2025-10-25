package com.roni.class12thagjetnotes.quiz

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ActivityQuizPlayBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.models.quiz.QuizDetail
import com.roni.class12thagjetnotes.models.quiz.QuizResult
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.collections.get

class QuizPlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizPlayBinding
    private var quizDetail: QuizDetail? = null
    private var currentQuestionIndex = 0
    private val userAnswers = mutableMapOf<String, Int>()
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var startTime: Long = 0

    companion object {
        const val EXTRA_QUIZ_ID = "quiz_id"
        const val EXTRA_QUIZ_TITLE = "quiz_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quizId = intent.getStringExtra(EXTRA_QUIZ_ID) ?: ""
        val quizTitle = intent.getStringExtra(EXTRA_QUIZ_TITLE) ?: ""

        setupUI()
        loadQuiz(quizId)
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener {
            showExitDialog()
        }

        binding.btnNext.setOnClickListener {
            moveToNextQuestion()
        }

        binding.btnPrevious.setOnClickListener {
            moveToPreviousQuestion()
        }

        binding.btnSubmit.setOnClickListener {
            showSubmitDialog()
        }

        // Option click listeners
        binding.option1Card.setOnClickListener { selectOption(0) }
        binding.option2Card.setOnClickListener { selectOption(1) }
        binding.option3Card.setOnClickListener { selectOption(2) }
        binding.option4Card.setOnClickListener { selectOption(3) }
    }

    private fun loadQuiz(quizId: String) {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val result = FirebaseManager.getQuizById(quizId)
            result.onSuccess { quiz ->
                quizDetail = quiz
                binding.progressBar.visibility = View.GONE
                startQuiz()
            }.onFailure {
                binding.progressBar.visibility = View.GONE
                showError("Failed to load quiz")
            }
        }
    }

    private fun startQuiz() {
        quizDetail?.let { quiz ->
            startTime = System.currentTimeMillis()
            timeLeftInMillis = quiz.duration.toLong() * 60 * 1000
            startTimer()
            displayQuestion()
            startAnimations()
        }
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                submitQuiz()
            }
        }.start()
    }

    private fun updateTimerText() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % 60
        binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)

        // Change color when time is running out
        if (minutes < 5) {
            binding.tvTimer.setTextColor(ContextCompat.getColor(this, R.color.error))
        }
    }

    private fun displayQuestion() {
        quizDetail?.let { quiz ->
            if (currentQuestionIndex < quiz.questions.size) {
                val question = quiz.questions[currentQuestionIndex]

                // Update progress
                binding.tvQuestionProgress.text = "${currentQuestionIndex + 1}/${quiz.questions.size}"
                binding.progressIndicator.progress =
                    ((currentQuestionIndex + 1) * 100) / quiz.questions.size

                // Display question
                binding.tvQuestion.text = question.question
                binding.tvQuestionMarks.text = "+${question.marks} marks"

                // Display options
                if (question.options.size >= 4) {
                    binding.tvOption1.text = question.options[0]
                    binding.tvOption2.text = question.options[1]
                    binding.tvOption3.text = question.options[2]
                    binding.tvOption4.text = question.options[3]
                }

                // Restore previous selection if any
                val previousAnswer = userAnswers[question.questionId]
                clearOptionSelection()
                if (previousAnswer != null) {
                    selectOption(previousAnswer, false)
                }

                // Show/hide buttons
                binding.btnPrevious.visibility = if (currentQuestionIndex > 0) View.VISIBLE else View.GONE
                binding.btnNext.visibility = if (currentQuestionIndex < quiz.questions.size - 1)
                    View.VISIBLE else View.GONE
                binding.btnSubmit.visibility = if (currentQuestionIndex == quiz.questions.size - 1)
                    View.VISIBLE else View.GONE

                // Animate question
                val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
                binding.questionCard.startAnimation(slideIn)
            }
        }
    }

    private fun selectOption(optionIndex: Int, animate: Boolean = true) {
        clearOptionSelection()

        val selectedCard = when (optionIndex) {
            0 -> binding.option1Card
            1 -> binding.option2Card
            2 -> binding.option3Card
            3 -> binding.option4Card
            else -> return
        }

        selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary))
        val textView = when (optionIndex) {
            0 -> binding.tvOption1
            1 -> binding.tvOption2
            2 -> binding.tvOption3
            3 -> binding.tvOption4
            else -> return
        }
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))

        // Save answer
        quizDetail?.questions?.get(currentQuestionIndex)?.let { question ->
            userAnswers[question.questionId] = optionIndex
        }

        if (animate) {
            val scale = AnimationUtils.loadAnimation(this, R.anim.scale_in)
            selectedCard.startAnimation(scale)
        }
    }

    private fun clearOptionSelection() {
        val cards = listOf(
            binding.option1Card, binding.option2Card,
            binding.option3Card, binding.option4Card
        )
        val textViews = listOf(
            binding.tvOption1, binding.tvOption2,
            binding.tvOption3, binding.tvOption4
        )

        cards.forEach {
            it.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface))
        }
        textViews.forEach {
            it.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
        }
    }

    private fun moveToNextQuestion() {
        quizDetail?.let { quiz ->
            if (currentQuestionIndex < quiz.questions.size - 1) {
                currentQuestionIndex++
                displayQuestion()
            }
        }
    }

    private fun moveToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            displayQuestion()
        }
    }

    private fun showSubmitDialog() {
        val unattempted = quizDetail?.questions?.size?.minus(userAnswers.size) ?: 0

        AlertDialog.Builder(this)
            .setTitle("Submit Quiz?")
            .setMessage("You have attempted ${userAnswers.size} questions.\n" +
                    if (unattempted > 0) "$unattempted questions are unattempted.\n\n" else "" +
                            "Are you sure you want to submit?")
            .setPositiveButton("Submit") { _, _ ->
                submitQuiz()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit Quiz?")
            .setMessage("Your progress will be lost. Are you sure?")
            .setPositiveButton("Exit") { _, _ ->
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun submitQuiz() {
        countDownTimer?.cancel()
        calculateAndSaveResult()
    }

    private fun calculateAndSaveResult() {
        quizDetail?.let { quiz ->
            var score = 0
            var correctAnswers = 0
            var incorrectAnswers = 0

            quiz.questions.forEach { question ->
                val userAnswer = userAnswers[question.questionId]
                if (userAnswer != null) {
                    if (userAnswer == question.correctAnswer) {
                        score += question.marks
                        correctAnswers++
                    } else {
                        score += question.negativeMarks
                        incorrectAnswers++
                    }
                }
            }

            val unattempted = quiz.questions.size - userAnswers.size
            val percentage = (score.toDouble() / quiz.maxMarks) * 100
            val timeTaken = ((System.currentTimeMillis() - startTime) / 1000 / 60).toInt()

            val result = QuizResult(
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                quizId = quiz.id,
                quizTitle = quiz.title,
                score = score,
                maxScore = quiz.maxMarks,
                percentage = percentage,
                correctAnswers = correctAnswers,
                incorrectAnswers = incorrectAnswers,
                unattempted = unattempted,
                timeTaken = timeTaken,
                completedAt = Timestamp.now(),
                answers = userAnswers
            )

            saveResult(result)
        }
    }

    private fun saveResult(result: QuizResult) {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val saveResult = FirebaseManager.saveQuizResult(result)
            binding.progressBar.visibility = View.GONE

            saveResult.onSuccess { resultId ->
                // Navigate to result screen
                val intent = Intent(this@QuizPlayActivity, QuizResultActivity::class.java)
                intent.putExtra("result_id", resultId)
                intent.putExtra("score", result.score)
                intent.putExtra("max_score", result.maxScore)
                intent.putExtra("percentage", result.percentage)
                intent.putExtra("correct", result.correctAnswers)
                intent.putExtra("incorrect", result.incorrectAnswers)
                intent.putExtra("unattempted", result.unattempted)
                intent.putExtra("time_taken", result.timeTaken)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }.onFailure {
                showError("Failed to save result")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.headerLayout.startAnimation(fadeIn)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showExitDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}