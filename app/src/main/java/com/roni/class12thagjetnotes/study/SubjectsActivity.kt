package com.roni.class12thagjetnotes.study

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.adapter.study.SubjectsAdapter
import com.roni.class12thagjetnotes.databinding.ActivitySubjectsBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.models.firebase.Subject
import kotlinx.coroutines.launch

class SubjectsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubjectsBinding
    private val subjects = mutableListOf<Subject>()
    private lateinit var adapter: SubjectsAdapter
    private var contentType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentType = intent.getStringExtra("content_type") ?: ""

        setupUI()
        setupRecyclerView()
        loadSubjects()
        startAnimations()
    }

    private fun setupUI() {
        // Set title based on content type
        binding.tvTitle.text = when (contentType) {
            "videos" -> "Select Subject for Videos"
            "pdfnotes" -> "Select Subject for PDF Notes"
            "books" -> "Select Subject for Books"
            "syllabus" -> "Select Subject for Syllabus"
            "pyqs" -> "Select Subject for Previous Papers"
            else -> "Select Subject"
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.swipeRefresh.setOnRefreshListener {
            loadSubjects()
        }
    }

    private fun setupRecyclerView() {
        adapter = SubjectsAdapter(subjects) { subject ->
            openContentActivity(subject)
        }

        binding.rvSubjects.layoutManager = GridLayoutManager(this, 2)
        binding.rvSubjects.adapter = adapter
    }

    private fun loadSubjects() {
        showLoading(true)

        lifecycleScope.launch {
            val result = FirebaseManager.getSubjects()

            result.onSuccess { subjectList ->
                subjects.clear()
                subjects.addAll(subjectList)
                adapter.notifyDataSetChanged()
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                updateUI()
            }.onFailure { error ->
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                loadDefaultSubjects() // Fallback
                showError(error.message ?: "Failed to load subjects")
            }
        }
    }

    private fun loadDefaultSubjects() {
        val defaultSubjects = listOf(
            Subject("1", "Mathematics", "📐", "#2196F3", "Numbers, Algebra, Calculus", 1),
            Subject("2", "Physics", "⚛️", "#FF9800", "Mechanics, Waves, Optics", 2),
            Subject("3", "Chemistry", "🧪", "#4CAF50", "Organic, Inorganic, Physical", 3),
            Subject("4", "Biology", "🧬", "#E91E63", "Botany, Zoology, Genetics", 4),
            Subject("5", "English", "📖", "#9C27B0", "Literature, Grammar, Writing", 5),
            Subject("6", "Computer Science", "💻", "#00BCD4", "Programming, Algorithms", 6)
        )

        subjects.clear()
        subjects.addAll(defaultSubjects)
        adapter.notifyDataSetChanged()
        updateUI()
    }

    private fun openContentActivity(subject: Subject) {
        val intent = when (contentType) {
            "videos" -> Intent(this, VideosActivity::class.java)
            "pdfnotes" -> Intent(this, PdfNotesActivity::class.java)
            "books" -> Intent(this, BooksActivity::class.java)
            "syllabus" -> Intent(this, SyllabusActivity::class.java)
            "pyqs" -> Intent(this, PreviousPapersActivity::class.java)
            else -> null
        }

        intent?.let {
            it.putExtra("subject_name", subject.name)
            it.putExtra("subject_id", subject.id)
            startActivity(it)
        }
    }

    private fun updateUI() {
        binding.tvTotalSubjects.text = "${subjects.size} Subjects Available"

        if (subjects.isEmpty()) {
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
        binding.rvSubjects.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.rvSubjects.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.headerLayout.startAnimation(fadeIn)
    }
}