package com.roni.class12thagjetnotes.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.adapter.study.ChaptersAdapter
import com.roni.class12thagjetnotes.databinding.ActivityQuizChaptersBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.models.firebase.Chapter
import kotlinx.coroutines.launch

class QuizChaptersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizChaptersBinding
    private val chapters = mutableListOf<Chapter>()
    private lateinit var adapter: ChaptersAdapter
    private var subjectId: String = ""
    private var subjectName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizChaptersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subjectId = intent.getStringExtra("subject_id") ?: ""
        subjectName = intent.getStringExtra("subject_name") ?: ""

        setupUI()
        setupRecyclerView()
        loadChapters()
        startAnimations()
    }

    private fun setupUI() {
        binding.tvTitle.text = "$subjectName - Chapters"

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.swipeRefresh.setOnRefreshListener {
            loadChapters()
        }
    }

    private fun setupRecyclerView() {
        adapter = ChaptersAdapter(chapters) { chapter ->
            openChapterQuizzes(chapter)
        }

        binding.rvChapters.layoutManager = GridLayoutManager(this, 2)
        binding.rvChapters.adapter = adapter
    }

    private fun loadChapters() {
        showLoading(true)

        lifecycleScope.launch {
            val result = FirebaseManager.getChaptersBySubject(subjectId)

            result.onSuccess { chapterList ->
                chapters.clear()
                chapters.addAll(chapterList)
                adapter.notifyDataSetChanged()
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                updateUI()
            }.onFailure { error ->
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                loadDefaultChapters() // Fallback
                showError(error.message ?: "Failed to load chapters")
            }
        }
    }

    private fun loadDefaultChapters() {
        // Create default chapters based on subject
        val defaultChapters = when (subjectName.lowercase()) {
            "mathematics" -> listOf(
                Chapter("ch1", subjectId, 1, "Relations and Functions", "Domain, Range, Types of Relations", 5, ""),
                Chapter("ch2", subjectId, 2, "Inverse Trigonometry", "Principal Values, Properties", 4, ""),
                Chapter("ch3", subjectId, 3, "Matrices", "Types, Operations, Determinants", 6, ""),
                Chapter("ch4", subjectId, 4, "Continuity", "Limits, Differentiability", 5, ""),
                Chapter("ch5", subjectId, 5, "Applications of Derivatives", "Rate of Change, Tangents", 7, ""),
                Chapter("ch6", subjectId, 6, "Integrals", "Integration Methods", 8, "")
            )
            "physics" -> listOf(
                Chapter("ch1", subjectId, 1, "Electric Charges", "Coulomb's Law, Electric Field", 5, ""),
                Chapter("ch2", subjectId, 2, "Electrostatic Potential", "Capacitance, Energy", 4, ""),
                Chapter("ch3", subjectId, 3, "Current Electricity", "Ohm's Law, Kirchhoff's Rules", 6, ""),
                Chapter("ch4", subjectId, 4, "Moving Charges", "Magnetic Force, Lorentz Force", 5, "")
            )
            "chemistry" -> listOf(
                Chapter("ch1", subjectId, 1, "Solid State", "Crystal Lattice, Packing", 4, ""),
                Chapter("ch2", subjectId, 2, "Solutions", "Concentration, Colligative Properties", 5, ""),
                Chapter("ch3", subjectId, 3, "Electrochemistry", "Cells, Nernst Equation", 6, ""),
                Chapter("ch4", subjectId, 4, "Chemical Kinetics", "Rate Laws, Order", 5, "")
            )
            else -> listOf(
                Chapter("ch1", subjectId, 1, "Chapter 1", "Introduction", 3, ""),
                Chapter("ch2", subjectId, 2, "Chapter 2", "Advanced Topics", 4, "")
            )
        }

        chapters.clear()
        chapters.addAll(defaultChapters)
        adapter.notifyDataSetChanged()
        updateUI()
    }

    private fun openChapterQuizzes(chapter: Chapter) {
        val intent = Intent(this, QuizListActivity::class.java)
        intent.putExtra("chapter_id", chapter.id)
        intent.putExtra("chapter_name", chapter.name)
        intent.putExtra("subject_name", subjectName)
        startActivity(intent)
    }

    private fun updateUI() {
        binding.tvTotalChapters.text = "${chapters.size} Chapters Available"

        if (chapters.isEmpty()) {
            showEmptyState()
        } else {
            hideEmptyState()
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.rvChapters.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.rvChapters.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.headerLayout.startAnimation(fadeIn)
    }
}