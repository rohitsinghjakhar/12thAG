package com.roni.class12thagjetnotes.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.roni.class12thagjetnotes.databinding.ActivityQuizListBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.models.Quiz
import com.roni.class12thagjetnotes.adapter.QuizListAdapter

class QuizListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizListBinding
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var quizListAdapter: QuizListAdapter
    private var chapterId: String = ""
    private var chapterName: String = ""
    private var subjectName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        chapterId = intent.getStringExtra("chapter_id") ?: ""
        chapterName = intent.getStringExtra("chapter_name") ?: ""
        subjectName = intent.getStringExtra("subject_name") ?: ""

        setupToolbar()
        initializeRecyclerView()
        setupFirebase()
        loadQuizzes()
    }

    private fun setupToolbar() {
        binding.toolbar.title = "$subjectName - $chapterName"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initializeRecyclerView() {
        quizListAdapter = QuizListAdapter { quiz ->
            navigateToQuizPlay(quiz)
        }

        binding.quizzesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@QuizListActivity)
            adapter = quizListAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupFirebase() {
        firebaseManager = FirebaseManager()
    }

    private fun loadQuizzes() {
        showLoading(true)

        firebaseManager.getQuizzesByChapter(chapterId) { quizzes, error ->
            showLoading(false)

            if (error != null) {
                showError("Failed to load quizzes: ${error.message}")
                return@getQuizzesByChapter
            }

            if (quizzes.isNullOrEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
                quizListAdapter.submitList(quizzes)
            }
        }
    }

    private fun navigateToQuizPlay(quiz: Quiz) {
        val intent = Intent(this, QuizPlayActivity::class.java).apply {
            putExtra("quiz_id", quiz.id)
            putExtra("quiz_title", quiz.title)
            putExtra("quiz_description", quiz.description)
            putExtra("total_questions", quiz.totalQuestions)
            putExtra("time_limit", quiz.timeLimit)
            putExtra("chapter_name", chapterName)
            putExtra("subject_name", subjectName)
        }
        startActivity(intent)
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.quizzesRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(show: Boolean) {
        binding.emptyStateLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.quizzesRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.emptyStateText.text = message
        showEmptyState(true)
    }
}