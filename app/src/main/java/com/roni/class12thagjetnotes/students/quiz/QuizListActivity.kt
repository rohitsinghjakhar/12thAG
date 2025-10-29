package com.roni.class12thagjetnotes.students.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.roni.class12thagjetnotes.databinding.ActivityQuizListBinding
import com.roni.class12thagjetnotes.students.adapter.QuizAdapter
import com.roni.class12thagjetnotes.students.models.Quiz
import com.roni.class12thagjetnotes.students.viewmodels.StudentDeskViewModel

class QuizListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizListBinding
    private lateinit var viewModel: StudentDeskViewModel
    private lateinit var quizAdapter: QuizAdapter
    private var chapterId: String = ""
    private var chapterName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chapterId = intent.getStringExtra("chapter_id") ?: ""
        chapterName = intent.getStringExtra("chapter_name") ?: "Quizzes"

        if (chapterId.isEmpty()) {
            Toast.makeText(this, "Invalid chapter", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel = ViewModelProvider(this)[StudentDeskViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        viewModel.loadQuizzes(chapterId)
    }

    private fun setupToolbar() {
        binding.toolbar.title = chapterName
        binding.toolbar.subtitle = "Available Quizzes"
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        quizAdapter = QuizAdapter { quiz ->
            navigateToQuiz(quiz)
        }

        binding.quizzesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@QuizListActivity)
            adapter = quizAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.quizzes.observe(this) { quizzes ->
            if (quizzes.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                quizAdapter.submitList(quizzes)
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

    private fun navigateToQuiz(quiz: Quiz) {
        val intent = Intent(this, QuizTakeActivity::class.java).apply {
            putExtra("quiz_id", quiz.id)
            putExtra("quiz_title", quiz.title)
            putExtra("time_limit", quiz.timeLimit)
            putExtra("passing_score", quiz.passingScore)
        }
        startActivity(intent)
    }

    // ✅ FIXED: Access the included layout's root view
    private fun showEmptyState() {
        binding.emptyStateLayout.root.visibility = View.VISIBLE
        binding.quizzesRecyclerView.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.root.visibility = View.GONE
        binding.quizzesRecyclerView.visibility = View.VISIBLE
    }
}
