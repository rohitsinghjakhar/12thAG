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
import com.roni.class12thagjetnotes.R
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
        tipsAdapter = JetTipsAdapter(tipsList) { tip ->
            showTipDialog(tip)
        }

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

    private fun showTipDialog(tip: JetTip) {
        AlertDialog.Builder(this)
            .setTitle(tip.title)
            .setMessage(tip.description)
            .setPositiveButton("Got it") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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