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
import com.google.firebase.firestore.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.roni.class12thagjetnotes.jet.adapters.JetTipsAdapter
import com.roni.class12thagjetnotes.jet.models.JetTip
import kotlinx.android.synthetic.main.activity_jet_tips.*

class JetTipsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var tipsAdapter: JetTipsAdapter
    private val tipsList = mutableListOf<JetTip>()

    private lateinit var currentSubject: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_tips)

        // Get subject from intent
        currentSubject = intent.getStringExtra("SUBJECT") ?: "All"

        db = Firebase.firestore

        setupToolbar()
        setupRecyclerView()
        loadTips()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$currentSubject - Tips & Tricks"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        tipsAdapter = JetTipsAdapter(tipsList) { tip, action ->
            when (action) {
                "view" -> viewTipDetails(tip)
                "like" -> likeTip(tip)
                "share" -> shareTip(tip)
            }
        } as (JetTip) -> Unit

        tipsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@JetTipsActivity)
            adapter = tipsAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadTips() {
        showLoading(true)

        db.collection("jet_materials")
            .document("tips_tricks")
            .collection("items")
            .whereEqualTo("subject", currentSubject)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                tipsList.clear()

                for (document in documents) {
                    val tip = document.toObject(JetTip::class.java)
                    tip.id = document.id
                    tipsList.add(tip)
                }

                tipsAdapter.notifyDataSetChanged()
                showLoading(false)
                updateEmptyState()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Error loading tips", Toast.LENGTH_SHORT).show()
                showLoading(false)
                updateEmptyState()
            }
    }

    private fun viewTipDetails(tip: JetTip) {
        AlertDialog.Builder(this)
            .setTitle(tip.title)
            .setMessage("""
                Category: ${tip.category}
                ${if (tip.author.isNotEmpty()) "By: ${tip.author}" else ""}
                
                ${tip.content}
            """.trimIndent())
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun likeTip(tip: JetTip) {
        db.collection("jet_materials")
            .document("tips_tricks")
            .collection("items")
            .document(tip.id)
            .get()
            .addOnSuccessListener { document ->
                val currentLikes = document.getLong("likes")?.toInt() ?: 0
                db.collection("jet_materials")
                    .document("tips_tricks")
                    .collection("items")
                    .document(tip.id)
                    .update("likes", currentLikes + 1)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show()
                        loadTips() // Reload to update UI
                    }
            }
    }

    private fun shareTip(tip: JetTip) {
        val shareText = """
            ${tip.title}
            
            ${tip.content}
            
            - Shared from JET Taiyari App
        """.trimIndent()

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(shareIntent, "Share Tip"))
    }

    private fun showLoading(show: Boolean) {
        progressLoading.visibility = if (show) View.VISIBLE else View.GONE
        tipsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun updateEmptyState() {
        if (tipsList.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            tipsRecyclerView.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            tipsRecyclerView.visibility = View.VISIBLE
        }
    }
}