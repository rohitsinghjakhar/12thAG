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
import kotlinx.android.synthetic.main.activity_jet_syllabus.*

class JetSyllabusActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var syllabusAdapter: JetSyllabusAdapter
    private val syllabusList = mutableListOf<JetSyllabusTopic>()

    private lateinit var currentSubject: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_syllabus)

        // Get subject from intent
        currentSubject = intent.getStringExtra("SUBJECT") ?: "All"

        db = Firebase.firestore

        setupToolbar()
        setupRecyclerView()
        loadSyllabus()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$currentSubject - Syllabus"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        syllabusAdapter = JetSyllabusAdapter(syllabusList) { topic ->
            showTopicDetails(topic)
        }

        syllabusRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@JetSyllabusActivity)
            adapter = syllabusAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadSyllabus() {
        showLoading(true)

        db.collection("jet_materials")
            .document("syllabus")
            .collection("items")
            .whereEqualTo("subject", currentSubject)
            .orderBy("unitNumber", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                syllabusList.clear()

                for (document in documents) {
                    val topic = document.toObject(JetSyllabusTopic::class.java)
                    topic.id = document.id
                    syllabusList.add(topic)
                }

                syllabusAdapter.notifyDataSetChanged()
                showLoading(false)
                updateEmptyState()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Error loading syllabus", Toast.LENGTH_SHORT).show()
                showLoading(false)
                updateEmptyState()
            }
    }

    private fun showTopicDetails(topic: JetSyllabusTopic) {
        val topicsText = topic.topics.joinToString("\n• ", "• ")
        val message = """
            Unit: ${topic.unit}
            Importance: ${topic.importance}
            Weightage: ${topic.weightage}
            
            Topics:
            $topicsText
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(topic.title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLoading(show: Boolean) {
        progressLoading.visibility = if (show) View.VISIBLE else View.GONE
        syllabusRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun updateEmptyState() {
        if (syllabusList.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            syllabusRecyclerView.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            syllabusRecyclerView.visibility = View.VISIBLE
        }
    }
}