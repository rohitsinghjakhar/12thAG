package com.roni.class12thagjetnotes.jet.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_jet_videos.*

class JetVideosActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var videoAdapter: JetVideoAdapter
    private val videoList = mutableListOf<JetVideo>()

    private lateinit var currentSubject: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jet_videos)

        // Get subject from intent
        currentSubject = intent.getStringExtra("SUBJECT") ?: "All"

        db = Firebase.firestore

        setupToolbar()
        setupRecyclerView()
        loadVideos()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$currentSubject - Videos"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        videoAdapter = JetVideoAdapter(videoList) { video ->
            playVideo(video)
        }

        videosRecyclerView.apply {
            layoutManager = GridLayoutManager(this@JetVideosActivity, 2)
            adapter = videoAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadVideos() {
        showLoading(true)

        db.collection("jet_materials")
            .document("videos")
            .collection("items")
            .whereEqualTo("subject", currentSubject)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                videoList.clear()

                for (document in documents) {
                    val video = document.toObject(JetVideo::class.java)
                    video.id = document.id
                    videoList.add(video)
                }

                videoAdapter.notifyDataSetChanged()
                showLoading(false)
                updateEmptyState()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Error loading videos", Toast.LENGTH_SHORT).show()
                showLoading(false)
                updateEmptyState()
            }
    }

    private fun playVideo(video: JetVideo) {
        if (video.isPremium && !isUserPremium()) {
            showPremiumDialog()
            return
        }

        incrementViewCount(video.id)

        when {
            video.youtubeId.isNotEmpty() -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${video.youtubeId}"))
                startActivity(intent)
            }
            video.videoUrl.isNotEmpty() -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.videoUrl))
                startActivity(intent)
            }
            else -> {
                Toast.makeText(this, "Video not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun incrementViewCount(videoId: String) {
        db.collection("jet_materials")
            .document("videos")
            .collection("items")
            .document(videoId)
            .get()
            .addOnSuccessListener { document ->
                val currentCount = document.getLong("viewCount")?.toInt() ?: 0
                db.collection("jet_materials")
                    .document("videos")
                    .collection("items")
                    .document(videoId)
                    .update("viewCount", currentCount + 1)
            }
    }

    private fun isUserPremium(): Boolean = false

    private fun showPremiumDialog() {
        AlertDialog.Builder(this)
            .setTitle("Premium Content")
            .setMessage("This video is available for premium users only.")
            .setPositiveButton("Upgrade") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showLoading(show: Boolean) {
        progressLoading.visibility = if (show) View.VISIBLE else View.GONE
        videosRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun updateEmptyState() {
        if (videoList.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            videosRecyclerView.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            videosRecyclerView.visibility = View.VISIBLE
        }
    }
}