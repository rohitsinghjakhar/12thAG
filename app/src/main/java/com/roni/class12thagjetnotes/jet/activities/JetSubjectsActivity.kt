package com.roni.class12thagjetnotes.jet.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ActivityJetSubjectsBinding
import com.roni.class12thagjetnotes.jet.adapters.JetSubjectsAdapter

class JetSubjectsActivity : AppCompatActivity() {

    private lateinit var subjectsAdapter: JetSubjectsAdapter
    private val subjectsList = mutableListOf<Subject>()
    private lateinit var binding: ActivityJetSubjectsBinding
    private lateinit var sectionType: String
    private lateinit var sectionTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJetSubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get section type from intent
        sectionType = intent.getStringExtra("SECTION_TYPE") ?: "pdf_notes"
        sectionTitle = intent.getStringExtra("SECTION_TITLE") ?: "PDF Notes"

        setupToolbar()
        setupSubjects()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = sectionTitle

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Update subtitle
        binding.tvSubtitle.text = "Select your subject"
    }

    private fun setupSubjects() {
        subjectsList.clear()

        // Define all subjects with their icons and colors
        subjectsList.add(Subject(
            "Agriculture",
            "🌾",
            "#4CAF50",
            "Complete agriculture concepts"
        ))

        subjectsList.add(Subject(
            "Biology",
            "🧬",
            "#2196F3",
            "Life sciences and biology"
        ))

        subjectsList.add(Subject(
            "Chemistry",
            "⚗️",
            "#9C27B0",
            "Organic, Inorganic & Physical"
        ))

        subjectsList.add(Subject(
            "Mathematics",
            "📐",
            "#FF9800",
            "Algebra, Calculus & more"
        ))

        subjectsList.add(Subject(
            "Physics",
            "⚛️",
            "#F44336",
            "Mechanics, Optics & more"
        ))
    }

    private fun setupRecyclerView() {
        subjectsAdapter = JetSubjectsAdapter(subjectsList) { subject ->
            navigateToSection(subject.name)
        }

        binding.subjectsRecyclerView.apply {
            layoutManager = GridLayoutManager(this@JetSubjectsActivity, 2)
            adapter = subjectsAdapter
            setHasFixedSize(true)
        }
    }

    private fun navigateToSection(subjectName: String) {
        val intent = when (sectionType) {
            "pdf_notes" -> Intent(this, JetPdfNotesActivity::class.java)
            "videos" -> Intent(this, JetVideosActivity::class.java)
            "pyqs" -> Intent(this, JetPyqsActivity::class.java)
            "quizzes" -> Intent(this, JetQuizzesActivity::class.java)
            "syllabus" -> Intent(this, JetSyllabusActivity::class.java)
            "mind_maps" -> Intent(this, JetMindMapsActivity::class.java)
            "tips_tricks" -> Intent(this, JetTipsActivity::class.java)
            else -> null
        }

        intent?.let {
            it.putExtra("SUBJECT", subjectName)
            it.putExtra("SECTION_TYPE", sectionType)
            startActivity(it)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

// Subject data class
data class Subject(
    val name: String,
    val icon: String,
    val colorHex: String,
    val description: String
)