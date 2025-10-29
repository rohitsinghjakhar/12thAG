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
import kotlinx.android.synthetic.main.activity_jet_pyqs.*

class JetPyqsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var pyqAdapter: JetPyqAdapter
    private val pyqList = mutableListOf<JetPyq>()

    private lateinit var currentSubject: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_pyqs)

        // Get subject from intent
        currentSubject = intent.getStringExtra("SUBJECT") ?: "All"

        db = Firebase.firestore

        setupToolbar()
        setupRecyclerView()
        loadPyqs()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$currentSubject - PYQs"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        pyqAdapter = JetPyqAdapter(pyqList) { pyq ->
            openPyq(pyq)
        }

        pyqsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@JetPyqsActivity)
            adapter = pyqAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadPyqs() {
        showLoading(true)

        db.collection("jet_materials")
            .document("pyqs")
            .collection("items")
            .whereEqualTo("subject", currentSubject)
            .orderBy("year", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                pyqList.clear()

                for (document in documents) {
                    val pyq = document.toObject(JetPyq::class.java)
                    pyq.id = document.id
                    pyqList.add(pyq)
                }

                pyqAdapter.notifyDataSetChanged()
                showLoading(false)
                updateEmptyState()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Error loading PYQs", Toast.LENGTH_SHORT).show()
                showLoading(false)
                updateEmptyState()
            }
    }

    private fun openPyq(pyq: JetPyq) {
        if (pyq.isPremium && !isUserPremium()) {
            showPremiumDialog()
            return
        }

        incrementDownloadCount(pyq.id)

        if (pyq.pdfUrl.isNotEmpty()) {
            val intent = Intent(this, JetPdfViewerActivity::class.java)
            intent.putExtra("PDF_URL", pyq.pdfUrl)
            intent.putExtra("PDF_TITLE", pyq.title)
            intent.putExtra("HAS_SOLUTIONS", pyq.hasSolutions)
            intent.putExtra("SOLUTIONS_URL", pyq.solutionsUrl)
            startActivity(intent)
        } else {
            Toast.makeText(this, "PYQ not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun incrementDownloadCount(pyqId: String) {
        db.collection("jet_materials")
            .document("pyqs")
            .collection("items")
            .document(pyqId)
            .get()
            .addOnSuccessListener { document ->
                val currentCount = document.getLong("downloadCount")?.toInt() ?: 0
                db.collection("jet_materials")
                    .document("pyqs")
                    .collection("items")
                    .document(pyqId)
                    .update("downloadCount", currentCount + 1)
            }
    }

    private fun isUserPremium(): Boolean = false

    private fun showPremiumDialog() {
        AlertDialog.Builder(this)
            .setTitle("Premium Content")
            .setMessage("This PYQ is available for premium users only.")
            .setPositiveButton("Upgrade") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showLoading(show: Boolean) {
        progressLoading.visibility = if (show) View.VISIBLE else View.GONE
        pyqsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun updateEmptyState() {
        if (pyqList.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            pyqsRecyclerView.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            pyqsRecyclerView.visibility = View.VISIBLE
        }
    }
}