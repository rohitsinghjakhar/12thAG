package com.roni.class12thagjetnotes.study

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.roni.class12thagjetnotes.adapter.VideosAdapter
import com.roni.class12thagjetnotes.databinding.ActivityVideosBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.models.firebase.Video
import kotlinx.coroutines.launch

class VideosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideosBinding
    private val allVideos = mutableListOf<Video>()
    private val filteredVideos = mutableListOf<Video>()
    private lateinit var adapter: VideosAdapter
    private var selectedSubject: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedSubject = intent.getStringExtra("subject_name") ?: "All"

        setupUI()
        setupRecyclerView()
        loadVideos()
    }

    private fun setupUI() {
        // Fixed: Using tvTitle with proper null safety
        binding.tvTitle?.text = if (selectedSubject != "All") {
            "$selectedSubject Videos"
        } else {
            "All Videos"
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterVideos(newText ?: "")
                return true
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            loadVideos()
        }
    }

    private fun setupRecyclerView() {
        adapter = VideosAdapter(filteredVideos) { video ->
            playVideo(video)
        }

        binding.rvVideos.layoutManager = LinearLayoutManager(this)
        binding.rvVideos.adapter = adapter
    }

    private fun loadVideos() {
        showLoading(true)

        lifecycleScope.launch {
            val result = FirebaseManager.getVideos(
                if (selectedSubject != "All") selectedSubject else null
            )

            result.onSuccess { videos ->
                allVideos.clear()
                allVideos.addAll(videos)
                filteredVideos.clear()
                filteredVideos.addAll(videos)
                adapter.notifyDataSetChanged()
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                updateUI()
            }.onFailure { error ->
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                showEmptyState()
                showError(error.message ?: "Failed to load videos")
            }
        }
    }

    private fun filterVideos(query: String) {
        val filtered = if (query.isEmpty()) {
            allVideos
        } else {
            allVideos.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.instructor.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }

        filteredVideos.clear()
        filteredVideos.addAll(filtered)
        adapter.notifyDataSetChanged()
        updateUI()
    }

    private fun playVideo(video: Video) {
        lifecycleScope.launch {
            FirebaseManager.incrementVideoViews(video.id)
        }

        if (video.videoUrl.isEmpty()) {
            showError("Video URL not available")
            return
        }

        // Open YouTube or other video URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.videoUrl))
        try {
            startActivity(intent)
        } catch (e: Exception) {
            showError("Cannot open video: ${e.message}")
        }
    }

    private fun updateUI() {
        binding.tvTotalVideos?.text = "${filteredVideos.size} Videos"

        if (filteredVideos.isEmpty()) {
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
        binding.rvVideos.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.rvVideos.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}