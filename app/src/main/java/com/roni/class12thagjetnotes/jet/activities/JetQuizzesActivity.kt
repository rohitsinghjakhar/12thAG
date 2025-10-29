package com.roni.class12thagjetnotes.jet.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_jet_quizzes.*

class JetQuizzesActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var quizAdapter: JetQuizAdapter
    private val quizList = mutableListOf<JetQuiz>()

    private lateinit var currentSubject: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_quizzes)

        // Get subject from intent
        currentSubject = intent.getStringExtra("SUBJECT") ?: "All"

        db = Firebase.firestore

        setupToolbar()
        setupRecyclerView()
        loadQuizzes()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$currentSubject - Quizzes"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        quizAdapter = JetQuizAdapter(quizList) { quiz ->
            startQuiz(quiz)
        }

        quizzesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@JetQuizzesActivity)
            adapter = quizAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadQuizzes() {
        showLoading(true)

        db.collection("jet_materials")
            .document("quizzes")
            .collection("items")
            .whereEqualTo("subject", currentSubject)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                quizList.clear()

                for (document in documents) {
                    val quiz = document.toObject(JetQuiz::class.java)
                    quiz.id = document.id
                    quizList.add(quiz)
                }

                quizAdapter.notifyDataSetChanged()
                showLoading(false)
                updateEmptyState()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Error loading quizzes", Toast.LENGTH_SHORT).show()
                showLoading(false)
                updateEmptyState()
            }
    }

    private fun startQuiz(quiz: JetQuiz) {
        if (quiz.isPremium && !isUserPremium()) {
            showPremiumDialog()
            return
        }

        // Show quiz details dialog
        showQuizDetailsDialog(quiz)
    }

    private fun showQuizDetailsDialog(quiz: JetQuiz) {
        val message = """
            Subject: ${quiz.subject}
            Topic: ${quiz.topic}
            Questions: ${quiz.totalQuestions}
            Duration: ${quiz.duration} minutes
            Difficulty: ${quiz.difficulty}
            Total Marks: ${quiz.totalMarks}
            
            Are you ready to start?
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(quiz.title)
            .setMessage(message)
            .setPositiveButton("Start Quiz") { dialog, _ ->
                dialog.dismiss()
                // Navigate to quiz screen
                navigateToQuizScreen(quiz)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToQuizScreen(quiz: JetQuiz) {
        // TODO: Implement quiz screen navigation
        Toast.makeText(this, "Quiz screen coming soon!", Toast.LENGTH_SHORT).show()

        // Example navigation:
        // val intent = Intent(this, JetQuizScreenActivity::class.java)
        // intent.putExtra("QUIZ_ID", quiz.quizId)
        // startActivity(intent)
    }

    private fun isUserPremium(): Boolean = false

    private fun showPremiumDialog() {
        AlertDialog.Builder(this)
            .setTitle("Premium Content")
            .setMessage("This quiz is available for premium users only.")
            .setPositiveButton("Upgrade") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showLoading(show: Boolean) {
        progressLoading.visibility = if (show) View.VISIBLE else View.GONE
        quizzesRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun updateEmptyState() {
        if (quizList.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            quizzesRecyclerView.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            quizzesRecyclerView.visibility = View.VISIBLE
        }
    }
}