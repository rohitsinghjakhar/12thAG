package com.roni.class12thagjetnotes.jet.activities

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_jet_pdf_notes.*

class JetPdfNotesActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var pdfAdapter: JetPdfAdapter
    private val pdfList = mutableListOf<JetPdfNote>()

    private var selectedSubject = "All"
    private lateinit var currentSubject: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_pdf_notes)

        // Get subject from intent
        currentSubject = intent.getStringExtra("SUBJECT") ?: "All"
        selectedSubject = currentSubject

        // Initialize Firebase
        db = Firebase.firestore

        // Setup Toolbar
        setupToolbar()

        // Setup RecyclerView
        setupRecyclerView()

        // Load data
        loadPdfNotes()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$currentSubject - PDF Notes"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        pdfAdapter = JetPdfAdapter(pdfList) { pdf ->
            // Handle PDF click - Open or Download
            handlePdfClick(pdf)
        }

        pdfRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@JetPdfNotesActivity)
            adapter = pdfAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadPdfNotes() {
        showLoading(true)

        val query = db.collection("jet_materials")
            .document("pdf_notes")
            .collection("items")
            .whereEqualTo("subject", currentSubject)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        query.get()
            .addOnSuccessListener { documents ->
                pdfList.clear()
                for (document in documents) {
                    val pdf = document.toObject(JetPdfNote::class.java)
                    pdf.id = document.id
                    pdfList.add(pdf)
                }
                pdfAdapter.notifyDataSetChanged()
                showLoading(false)

                // Update empty state
                updateEmptyState()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Error loading PDFs", Toast.LENGTH_SHORT).show()
                showLoading(false)
                updateEmptyState()
            }
    }

    private fun handlePdfClick(pdf: JetPdfNote) {
        if (pdf.isPremium && !isUserPremium()) {
            // Show premium dialog
            showPremiumDialog()
            return
        }

        // Update view count
        incrementViewCount(pdf.id)

        // Open PDF
        if (pdf.pdfUrl.isNotEmpty()) {
            val intent = Intent(this, JetPdfViewerActivity::class.java)
            intent.putExtra("PDF_URL", pdf.pdfUrl)
            intent.putExtra("PDF_TITLE", pdf.title)
            intent.putExtra("PDF_ID", pdf.id)
            startActivity(intent)
        } else {
            Toast.makeText(this, "PDF not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun incrementViewCount(pdfId: String) {
        db.collection("jet_materials")
            .document("pdf_notes")
            .collection("items")
            .document(pdfId)
            .get()
            .addOnSuccessListener { document ->
                val currentCount = document.getLong("viewCount")?.toInt() ?: 0
                db.collection("jet_materials")
                    .document("pdf_notes")
                    .collection("items")
                    .document(pdfId)
                    .update("viewCount", currentCount + 1)
            }
    }

    private fun isUserPremium(): Boolean {
        // Implement your premium user check logic
        // For now, return false
        return false
    }

    private fun showPremiumDialog() {
        AlertDialog.Builder(this)
            .setTitle("Premium Content")
            .setMessage("This content is available for premium users only. Upgrade to premium to access all materials.")
            .setPositiveButton("Upgrade") { dialog, _ ->
                // Navigate to premium subscription screen
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLoading(show: Boolean) {
        progressLoading.visibility = if (show) View.VISIBLE else View.GONE
        pdfRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun updateEmptyState() {
        if (pdfList.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            pdfRecyclerView.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            pdfRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}