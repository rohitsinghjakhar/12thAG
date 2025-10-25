package com.roni.class12thagjetnotes.quiz

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.roni.class12thagjetnotes.MainActivity
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ActivityQuizResultBinding
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class QuizResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizResultBinding
    private var score = 0
    private var maxScore = 0
    private var percentage = 0.0
    private var correct = 0
    private var incorrect = 0
    private var unattempted = 0
    private var timeTaken = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        score = intent.getIntExtra("score", 0)
        maxScore = intent.getIntExtra("max_score", 0)
        percentage = intent.getDoubleExtra("percentage", 0.0)
        correct = intent.getIntExtra("correct", 0)
        incorrect = intent.getIntExtra("incorrect", 0)
        unattempted = intent.getIntExtra("unattempted", 0)
        timeTaken = intent.getIntExtra("time_taken", 0)

        setupUI()
        displayResults()
        startAnimations()
    }

    private fun setupUI() {
        binding.btnHome.setOnClickListener {
            navigateToHome()
        }

        binding.btnReview.setOnClickListener {
            // TODO: Navigate to review answers screen
            showToast("Review feature coming soon!")
        }

        binding.btnShare.setOnClickListener {
            shareResult()
        }
    }

    private fun displayResults() {
        // Display score with animation
        animateScoreCounter(score)

        // Display percentage
        binding.tvPercentage.text = "${String.format("%.1f", percentage)}%"

        // Display grade and message
        val (grade, message, emoji, color) = getGradeInfo(percentage)
        binding.tvGrade.text = grade
        binding.tvResultMessage.text = message
        binding.tvEmoji.text = emoji
        binding.resultCard.setCardBackgroundColor(ContextCompat.getColor(this, color))

        // Display statistics
        binding.tvCorrectCount.text = correct.toString()
        binding.tvIncorrectCount.text = incorrect.toString()
        binding.tvUnattemptedCount.text = unattempted.toString()
        binding.tvTimeTaken.text = "$timeTaken min"
        binding.tvMaxScore.text = "/ $maxScore"

        // Animate circular progress
        animateCircularProgress(percentage.toInt())

        // Show confetti for good performance
        if (percentage >= 75) {
            showConfetti()
        }

        // Animate statistics cards
        animateStatCards()
    }

    private fun animateScoreCounter(targetScore: Int) {
        val animator = ObjectAnimator.ofInt(0, targetScore)
        animator.duration = 2000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            binding.tvScore.text = animation.animatedValue.toString()
        }
        animator.start()
    }

    private fun animateCircularProgress(targetProgress: Int) {
        val animator = ObjectAnimator.ofInt(binding.circularProgress, "progress", 0, targetProgress)
        animator.duration = 2000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }

    private fun getGradeInfo(percentage: Double): Quadruple {
        return when {
            percentage >= 90 -> Quadruple("A+", "Outstanding! 🎉", "🏆", R.color.success)
            percentage >= 80 -> Quadruple("A", "Excellent Work!", "⭐", R.color.primary)
            percentage >= 70 -> Quadruple("B+", "Very Good!", "😊", R.color.info)
            percentage >= 60 -> Quadruple("B", "Good Job!", "👍", R.color.accent)
            percentage >= 50 -> Quadruple("C", "Keep Practicing!", "💪", R.color.warning)
            percentage >= 40 -> Quadruple("D", "Need Improvement", "📚", R.color.warning)
            else -> Quadruple("F", "Don't Give Up!", "🔥", R.color.error)
        }
    }

    private data class Quadruple(
        val grade: String,
        val message: String,
        val emoji: String,
        val color: Int
    )

    private fun showConfetti() {
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 3, TimeUnit.SECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        binding.konfettiView.start(party)
    }

    private fun animateStatCards() {
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        binding.cardCorrect.startAnimation(slideUp)
        binding.cardIncorrect.postDelayed({
            binding.cardIncorrect.startAnimation(slideUp)
        }, 100)
        binding.cardUnattempted.postDelayed({
            binding.cardUnattempted.startAnimation(slideUp)
        }, 200)
        binding.cardTimeTaken.postDelayed({
            binding.cardTimeTaken.startAnimation(slideUp)
        }, 300)
    }

    private fun startAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in)
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)

        binding.resultCard.startAnimation(scaleIn)
        binding.statsLayout.postDelayed({
            binding.statsLayout.startAnimation(fadeIn)
        }, 500)
        binding.buttonLayout.postDelayed({
            binding.buttonLayout.startAnimation(slideDown)
        }, 1000)
    }

    private fun shareResult() {
        val shareText = """
            🎓 AGJet Notes - Quiz Result 🎓
            
            Score: $score / $maxScore
            Percentage: ${String.format("%.1f", percentage)}%
            
            ✅ Correct: $correct
            ❌ Incorrect: $incorrect
            ⏭️ Unattempted: $unattempted
            ⏱️ Time: $timeTaken minutes
            
            Keep learning with AGJet Notes!
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share Result"))
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToHome()
    }
}