package com.roni.class12thagjetnotes.jet

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.roni.class12thagjetnotes.jet.adapters.JetUpdatesAdapter
import com.roni.class12thagjetnotes.jet.models.JetUpdate
import kotlinx.android.synthetic.main.activity_jet_taiyari.*

class JetTaiyariActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var updatesAdapter: JetUpdatesAdapter
    private val updatesList = mutableListOf<JetUpdate>()

    private var pdfCount = 0
    private var videoCount = 0
    private var pyqCount = 0
    private var quizCount = 0
    private var syllabusCount = 0
    private var mindMapCount = 0
    private var tipsCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_taiyari)

        // Initialize Firebase
        db = Firebase.firestore

        // Setup Toolbar
        setupToolbar()

        // Setup RecyclerView
        setupUpdatesRecyclerView()

        // Load data from Firebase
        loadDataFromFirebase()

        // Setup Click Listeners
        setupClickListeners()

        // Setup FAB
        setupFab()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Collapsing Toolbar effect
        collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white, null))
        collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.transparent, null))
    }

    private fun setupUpdatesRecyclerView() {
        updatesAdapter = JetUpdatesAdapter(updatesList) { update ->
            // Handle update click
            handleUpdateClick(update)
        }

        updatesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@JetTaiyariActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = updatesAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        // PDF Notes
        cardPdfNotes.setOnClickListener {
            if (pdfCount > 0) {
                navigateToSection("pdf_notes")
            } else {
                showComingSoonMessage("PDF Notes")
            }
        }

        // Videos
        cardVideos.setOnClickListener {
            if (videoCount > 0) {
                navigateToSection("videos")
            } else {
                showComingSoonMessage("Videos")
            }
        }

        // PYQs
        cardPyqs.setOnClickListener {
            if (pyqCount > 0) {
                navigateToSection("pyqs")
            } else {
                showComingSoonMessage("Previous Year Questions")
            }
        }

        // Quizzes
        cardQuizzes.setOnClickListener {
            if (quizCount > 0) {
                navigateToSection("quizzes")
            } else {
                showComingSoonMessage("Quizzes")
            }
        }

        // Syllabus
        cardSyllabus.setOnClickListener {
            if (syllabusCount > 0) {
                navigateToSection("syllabus")
            } else {
                showComingSoonMessage("Syllabus")
            }
        }

        // Mind Maps
        cardMindMaps.setOnClickListener {
            if (mindMapCount > 0) {
                navigateToSection("mind_maps")
            } else {
                showComingSoonMessage("Mind Maps")
            }
        }

        // Tips & Tricks
        cardTips.setOnClickListener {
            if (tipsCount > 0) {
                navigateToSection("tips_tricks")
            } else {
                showComingSoonMessage("Tips & Tricks")
            }
        }

        // Exam Info Card
        cardExamInfo.setOnClickListener {
            navigateToExamInfo()
        }
    }

    private fun setupFab() {
        fabQuickAccess.setOnClickListener {
            // Navigate to quick practice or quiz
            if (quizCount > 0) {
                navigateToSection("quizzes")
            } else {
                Snackbar.make(it, "Practice materials coming soon!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDataFromFirebase() {
        showLoading(true)

        // Load counts
        loadCounts()

        // Load updates
        loadUpdates()

        // Load progress
        loadProgress()
    }

    private fun loadCounts() {
        // Load PDF Notes count
        db.collection("jet_materials")
            .document("pdf_notes")
            .collection("items")
            .get()
            .addOnSuccessListener { documents ->
                pdfCount = documents.size()
                tvPdfCount.text = "$pdfCount PDFs"
            }
            .addOnFailureListener { e ->
                handleFirebaseError("PDF Notes", e)
            }

        // Load Videos count
        db.collection("jet_materials")
            .document("videos")
            .collection("items")
            .get()
            .addOnSuccessListener { documents ->
                videoCount = documents.size()
                tvVideoCount.text = "$videoCount Videos"
            }
            .addOnFailureListener { e ->
                handleFirebaseError("Videos", e)
            }

        // Load PYQs count
        db.collection("jet_materials")
            .document("pyqs")
            .collection("items")
            .get()
            .addOnSuccessListener { documents ->
                pyqCount = documents.size()
                tvPyqCount.text = "$pyqCount Papers"
            }
            .addOnFailureListener { e ->
                handleFirebaseError("PYQs", e)
            }

        // Load Quizzes count
        db.collection("jet_materials")
            .document("quizzes")
            .collection("items")
            .get()
            .addOnSuccessListener { documents ->
                quizCount = documents.size()
                tvQuizCount.text = "$quizCount Quizzes"
            }
            .addOnFailureListener { e ->
                handleFirebaseError("Quizzes", e)
            }

        // Load Syllabus count
        db.collection("jet_materials")
            .document("syllabus")
            .collection("items")
            .get()
            .addOnSuccessListener { documents ->
                syllabusCount = documents.size()
                tvSyllabusCount.text = "$syllabusCount Topics"
            }
            .addOnFailureListener { e ->
                handleFirebaseError("Syllabus", e)
            }

        // Load Mind Maps count
        db.collection("jet_materials")
            .document("mind_maps")
            .collection("items")
            .get()
            .addOnSuccessListener { documents ->
                mindMapCount = documents.size()
                tvMindMapCount.text = "$mindMapCount Maps"
            }
            .addOnFailureListener { e ->
                handleFirebaseError("Mind Maps", e)
            }

        // Load Tips & Tricks count
        db.collection("jet_materials")
            .document("tips_tricks")
            .collection("items")
            .get()
            .addOnSuccessListener { documents ->
                tipsCount = documents.size()
                tvTipsCount.text = "$tipsCount Tips"
                showLoading(false)
            }
            .addOnFailureListener { e ->
                handleFirebaseError("Tips", e)
                showLoading(false)
            }
    }

    private fun loadUpdates() {
        db.collection("jet_updates")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                updatesList.clear()
                for (document in documents) {
                    val update = document.toObject(JetUpdate::class.java)
                    updatesList.add(update)
                }
                updatesAdapter.notifyDataSetChanged()

                // Show/hide empty state
                if (updatesList.isEmpty()) {
                    updatesRecyclerView.visibility = View.GONE
                } else {
                    updatesRecyclerView.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { e ->
                handleFirebaseError("Updates", e)
                updatesRecyclerView.visibility = View.GONE
            }
    }

    private fun loadProgress() {
        // Get user ID (assuming you have user authentication)
        val userId = getCurrentUserId()

        if (userId.isEmpty()) {
            progressCard.visibility = View.GONE
            return
        }

        db.collection("jet_user_progress")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val completed = document.getLong("completed")?.toInt() ?: 0
                    val inProgress = document.getLong("inProgress")?.toInt() ?: 0
                    val total = document.getLong("total")?.toInt() ?: 0

                    // Update UI
                    tvCompletedCount.text = completed.toString()
                    tvInProgressCount.text = inProgress.toString()
                    tvTotalCount.text = total.toString()

                    // Calculate percentage
                    val percentage = if (total > 0) (completed * 100) / total else 0
                    tvProgressPercent.text = "$percentage%"
                    progressBar.progress = percentage

                    progressCard.visibility = View.VISIBLE
                } else {
                    // Initialize progress for new user
                    initializeUserProgress(userId)
                }
            }
            .addOnFailureListener { e ->
                handleFirebaseError("Progress", e)
                progressCard.visibility = View.GONE
            }
    }

    private fun initializeUserProgress(userId: String) {
        val totalItems = pdfCount + videoCount + pyqCount + quizCount + syllabusCount + mindMapCount + tipsCount

        val progressData = hashMapOf(
            "completed" to 0,
            "inProgress" to 0,
            "total" to totalItems,
            "lastUpdated" to System.currentTimeMillis()
        )

        db.collection("jet_user_progress")
            .document(userId)
            .set(progressData)
            .addOnSuccessListener {
                tvCompletedCount.text = "0"
                tvInProgressCount.text = "0"
                tvTotalCount.text = totalItems.toString()
                tvProgressPercent.text = "0%"
                progressBar.progress = 0
                progressCard.visibility = View.VISIBLE
            }
    }

    private fun navigateToSection(section: String) {
        val intent = Intent(this, JetSubjectsActivity::class.java)

        val sectionTitle = when (section) {
            "pdf_notes" -> "PDF Notes"
            "videos" -> "Videos"
            "pyqs" -> "Previous Year Questions"
            "quizzes" -> "Quizzes"
            "syllabus" -> "Syllabus"
            "mind_maps" -> "Mind Maps"
            "tips_tricks" -> "Tips & Tricks"
            else -> "Study Materials"
        }

        intent.putExtra("SECTION_TYPE", section)
        intent.putExtra("SECTION_TITLE", sectionTitle)
        startActivity(intent)
    }

    private fun navigateToExamInfo() {
        val intent = Intent(this, JetExamInfoActivity::class.java)
        startActivity(intent)
    }

    private fun handleUpdateClick(update: JetUpdate) {
        // Navigate to update details or open link
        if (update.link.isNotEmpty()) {
            // Open link in browser or in-app browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(update.link))
            startActivity(intent)
        } else {
            // Show update details in dialog
            showUpdateDetailsDialog(update)
        }
    }

    private fun showUpdateDetailsDialog(update: JetUpdate) {
        AlertDialog.Builder(this)
            .setTitle(update.title)
            .setMessage(update.description)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showComingSoonMessage(feature: String) {
        Snackbar.make(
            findViewById(R.id.content),
            "$feature content will be available soon!",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showLoading(show: Boolean) {
        progressLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun handleFirebaseError(section: String, exception: Exception) {
        exception.printStackTrace()
        // Optionally show error to user
        // Toast.makeText(this, "Error loading $section", Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentUserId(): String {
        // Implement your user authentication logic
        // For now, returning empty string
        // You can use Firebase Auth: FirebaseAuth.getInstance().currentUser?.uid ?: ""
        return ""
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}