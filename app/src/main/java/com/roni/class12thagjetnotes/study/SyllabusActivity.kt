package com.roni.class12thagjetnotes.study

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.roni.class12thagjetnotes.adapter.SyllabusAdapter
import com.roni.class12thagjetnotes.databinding.ActivitySyllabusBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.models.firebase.Syllabus
import com.roni.class12thagjetnotes.students.viewers.PdfViewerActivity
import kotlinx.coroutines.launch

class SyllabusActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySyllabusBinding
    private val syllabusList = mutableListOf<Syllabus>()
    private lateinit var adapter: SyllabusAdapter
    private var selectedSubject: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySyllabusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedSubject = intent.getStringExtra("subject_name") ?: "All"

        setupUI()
        setupRecyclerView()
        loadSyllabus()
    }

    private fun setupUI() {
        binding.tvTitle.text = if (selectedSubject != "All") {
            "$selectedSubject Syllabus"
        } else {
            "All Syllabus"
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.swipeRefresh.setOnRefreshListener {
            loadSyllabus()
        }
    }

    private fun setupRecyclerView() {
        adapter = SyllabusAdapter(syllabusList) { syllabus ->
            openSyllabus(syllabus)
        }

        binding.rvSyllabus.layoutManager = LinearLayoutManager(this)
        binding.rvSyllabus.adapter = adapter
    }

    private fun loadSyllabus() {
        showLoading(true)

        lifecycleScope.launch {
            val result = FirebaseManager.getSyllabus(
                if (selectedSubject != "All") selectedSubject else null
            )

            result.onSuccess { syllabus ->
                syllabusList.clear()
                syllabusList.addAll(syllabus)
                adapter.notifyDataSetChanged()
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                updateUI()
            }.onFailure { error ->
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                loadDefaultSyllabus()
                showError(error.message ?: "Failed to load syllabus")
            }
        }
    }

    private fun loadDefaultSyllabus() {
        val defaultSyllabus = listOf(
            Syllabus(
                id = "syl1",
                title = "Complete Syllabus - Mathematics",
                subject = "Mathematics",
                description = "Full syllabus covering all chapters",
                pdfUrl = "syllabus/mathematics_full.pdf",
                topics = listOf(
                    "Relations and Functions",
                    "Inverse Trigonometric Functions",
                    "Matrices and Determinants",
                    "Continuity and Differentiability",
                    "Applications of Derivatives",
                    "Integrals",
                    "Applications of Integrals",
                    "Differential Equations",
                    "Vector Algebra",
                    "Three Dimensional Geometry",
                    "Linear Programming",
                    "Probability"
                ),
                totalMarks = 100,
                duration = "3 hours",
                examBoard = "CBSE",
                year = 2024
            ),
            Syllabus(
                id = "syl2",
                title = "Complete Syllabus - Physics",
                subject = "Physics",
                description = "Full syllabus covering all chapters",
                pdfUrl = "syllabus/physics_full.pdf",
                topics = listOf(
                    "Electric Charges and Fields",
                    "Electrostatic Potential and Capacitance",
                    "Current Electricity",
                    "Moving Charges and Magnetism",
                    "Magnetism and Matter",
                    "Electromagnetic Induction",
                    "Alternating Current",
                    "Electromagnetic Waves"
                ),
                totalMarks = 70,
                duration = "3 hours",
                examBoard = "CBSE",
                year = 2024
            )
        )

        syllabusList.clear()
        syllabusList.addAll(defaultSyllabus)
        adapter.notifyDataSetChanged()
        updateUI()
    }

    private fun openSyllabus(syllabus: Syllabus) {
        if (syllabus.pdfUrl.isEmpty()) {
            showError("Syllabus PDF not available")
            return
        }

        val intent = Intent(this, PdfViewerActivity::class.java)
        intent.putExtra("pdf_url", syllabus.pdfUrl)
        intent.putExtra("pdf_title", syllabus.title)
        startActivity(intent)
    }

    private fun updateUI() {
        binding.tvTotalSyllabus.text = "${syllabusList.size} Syllabus Items"

        if (syllabusList.isEmpty()) {
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
        binding.rvSyllabus.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.rvSyllabus.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}