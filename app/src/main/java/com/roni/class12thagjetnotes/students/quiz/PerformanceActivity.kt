package com.roni.class12thagjetnotes.students.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.roni.class12thagjetnotes.databinding.ActivityPerformanceBinding
import com.roni.class12thagjetnotes.students.adapter.PerformanceAdapter
import com.roni.class12thagjetnotes.students.models.QuizResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PerformanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerformanceBinding
    private lateinit var performanceAdapter: PerformanceAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var allResults = listOf<QuizResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupFilters()
        loadUserPerformance()
    }

    private fun setupToolbar() {
        binding.toolbar.title = "My Performance"
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        performanceAdapter = PerformanceAdapter { result ->
            // Can open detailed view if needed
        }

        binding.performanceRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PerformanceActivity)
            adapter = performanceAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupFilters() {
        binding.filterAll.setOnClickListener {
            showAllResults()
        }

        binding.filterPassed.setOnClickListener {
            showFilteredResults(true)
        }

        binding.filterFailed.setOnClickListener {
            showFilteredResults(false)
        }
    }

    private fun loadUserPerformance() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Please login to view performance", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val results = firestore.collection("quiz_results")
                    .whereEqualTo("userId", userId)
                    .orderBy("completedAt", Query.Direction.DESCENDING)
                    .get()
                    .await()

                allResults = results.documents.mapNotNull { doc ->
                    doc.toObject(QuizResult::class.java)?.copy(id = doc.id)
                }

                if (allResults.isEmpty()) {
                    showEmptyState()
                } else {
                    hideEmptyState()
                    displayResults(allResults)
                    calculateStatistics(allResults)
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@PerformanceActivity,
                    "Error loading performance: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun displayResults(results: List<QuizResult>) {
        performanceAdapter.submitList(results)
    }

    private fun calculateStatistics(results: List<QuizResult>) {
        val totalQuizzes = results.size
        val passedQuizzes = results.count { it.passed }
        val failedQuizzes = totalQuizzes - passedQuizzes

        val averageScore = if (totalQuizzes > 0) {
            results.map { it.score }.average().toInt()
        } else 0

        val totalQuestions = results.sumOf { it.totalQuestions }
        val correctAnswers = results.sumOf { it.correctAnswers }
        val accuracy = if (totalQuestions > 0) {
            (correctAnswers * 100 / totalQuestions)
        } else 0

        // Display statistics
        binding.totalQuizzesText.text = totalQuizzes.toString()
        binding.passedQuizzesText.text = passedQuizzes.toString()
        binding.failedQuizzesText.text = failedQuizzes.toString()
        binding.averageScoreText.text = "$averageScore%"
        binding.accuracyText.text = "$accuracy%"
        binding.totalQuestionsAttemptedText.text = totalQuestions.toString()

        // Update progress
        binding.averageScoreProgress.progress = averageScore
    }

    private fun showAllResults() {
        displayResults(allResults)
        highlightFilter(binding.filterAll)
    }

    private fun showFilteredResults(passed: Boolean) {
        val filteredResults = allResults.filter { it.passed == passed }
        displayResults(filteredResults)

        if (passed) {
            highlightFilter(binding.filterPassed)
        } else {
            highlightFilter(binding.filterFailed)
        }

        if (filteredResults.isEmpty()) {
            val message = if (passed) "No passed quizzes yet" else "No failed quizzes"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun highlightFilter(selectedView: View) {
        // Reset all filters
        binding.filterAll.isSelected = false
        binding.filterPassed.isSelected = false
        binding.filterFailed.isSelected = false

        // Highlight selected
        selectedView.isSelected = true
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.performanceRecyclerView.visibility = View.GONE
        binding.statisticsCard.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.performanceRecyclerView.visibility = View.VISIBLE
        binding.statisticsCard.visibility = View.VISIBLE
    }
}