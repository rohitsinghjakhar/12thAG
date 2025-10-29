package com.roni.class12thagjetnotes.jet.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.roni.class12thagjetnotes.jet.adapters.JetMindMapAdapter
import com.roni.class12thagjetnotes.jet.models.JetMindMap
import kotlinx.android.synthetic.main.activity_jet_mind_maps.*

class JetMindMapsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var mindMapAdapter: JetMindMapAdapter
    private val mindMapList = mutableListOf<JetMindMap>()

    private lateinit var currentSubject: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_mind_maps)

        // Get subject from intent
        currentSubject = intent.getStringExtra("SUBJECT") ?: "All"

        db = Firebase.firestore

        setupToolbar()
        setupRecyclerView()
        loadMindMaps()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$currentSubject - Mind Maps"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        mindMapAdapter = JetMindMapAdapter(mindMapList) { mindMap ->
            viewMindMap(mindMap)
        }

        mindMapsRecyclerView.apply {
            layoutManager = GridLayoutManager(this@JetMindMapsActivity, 2)
            adapter = mindMapAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadMindMaps() {
        showLoading(true)

        db.collection("jet_materials")
            .document("mind_maps")
            .collection("items")
            .whereEqualTo("subject", currentSubject)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                mindMapList.clear()

                for (document in documents) {
                    val mindMap = document.toObject(JetMindMap::class.java)
                    mindMap.id = document.id
                    mindMapList.add(mindMap)
                }

                mindMapAdapter.notifyDataSetChanged()
                showLoading(false)
                updateEmptyState()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Error loading mind maps", Toast.LENGTH_SHORT).show()
                showLoading(false)
                updateEmptyState()
            }
    }

    private fun viewMindMap(mindMap: JetMindMap) {
        if (mindMap.isPremium && !isUserPremium()) {
            showPremiumDialog()
            return
        }

        incrementViewCount(mindMap.id)

        // Navigate to image viewer activity
        if (mindMap.imageUrl.isNotEmpty()) {
            val intent = Intent(this, JetImageViewerActivity::class.java)
            intent.putExtra("IMAGE_URL", mindMap.imageUrl)
            intent.putExtra("TITLE", mindMap.title)
            intent.putExtra("PDF_URL", mindMap.pdfUrl)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Mind map not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun incrementViewCount(mindMapId: String) {
        db.collection("jet_materials")
            .document("mind_maps")
            .collection("items")
            .document(mindMapId)
            .get()
            .addOnSuccessListener { document ->
                val currentCount = document.getLong("viewCount")?.toInt() ?: 0
                db.collection("jet_materials")
                    .document("mind_maps")
                    .collection("items")
                    .document(mindMapId)
                    .update("viewCount", currentCount + 1)
            }
    }

    private fun isUserPremium(): Boolean = false

    private fun showPremiumDialog() {
        AlertDialog.Builder(this)
            .setTitle("Premium Content")
            .setMessage("This mind map is available for premium users only.")
            .setPositiveButton("Upgrade") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showLoading(show: Boolean) {
        progressLoading.visibility = if (show) View.VISIBLE else View.GONE
        mindMapsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun updateEmptyState() {
        if (mindMapList.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            mindMapsRecyclerView.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            mindMapsRecyclerView.visibility = View.VISIBLE
        }
    }
}