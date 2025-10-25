package com.roni.class12thagjetnotes.study

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.adapter.PreviousPapersAdapter
import com.roni.class12thagjetnotes.databinding.ActivityPreviousPapersBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.models.firebase.PreviousPaper
import com.roni.class12thagjetnotes.viewer.PdfViewerActivity
import kotlinx.coroutines.launch

class PreviousPapersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviousPapersBinding
    private val allPapers = mutableListOf<PreviousPaper>()
    private val filteredPapers = mutableListOf<PreviousPaper>()
    private lateinit var adapter: PreviousPapersAdapter
    private var selectedSubject: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviousPapersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedSubject = intent.getStringExtra("subject_name") ?: "All"

        setupUI()
        setupRecyclerView()
        loadPreviousPapers()
        startAnimations()
    }

    private fun setupUI() {
        binding.tvTitle.text = if (selectedSubject != "All") {
            "$selectedSubject Previous Papers"
        } else {
            "All Previous Papers"
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterPapers(newText ?: "")
                return true
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            loadPreviousPapers()
        }
    }

    private fun setupRecyclerView() {
        adapter = PreviousPapersAdapter(
            papers = filteredPapers,
            onItemClick = { paper ->
                openPaper(paper)
            },
            onDownloadClick = { paper ->
                downloadPaper(paper)
            }
        )

        binding.rvPreviousPapers.layoutManager = LinearLayoutManager(this)
        binding.rvPreviousPapers.adapter = adapter
    }

    private fun loadPreviousPapers() {
        showLoading(true)

        lifecycleScope.launch {
            val result = FirebaseManager.getPreviousPapers(
                if (selectedSubject != "All") selectedSubject else null
            )

            result.onSuccess { papers ->
                allPapers.clear()
                allPapers.addAll(papers)
                filteredPapers.clear()
                filteredPapers.addAll(papers)
                adapter.notifyDataSetChanged()
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                updateUI()
            }.onFailure { error ->
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                showEmptyState()
                showError(error.message ?: "Failed to load previous papers")
            }
        }
    }

    private fun filterPapers(query: String) {
        val filtered = if (query.isEmpty()) {
            allPapers
        } else {
            allPapers.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.year.contains(query, ignoreCase = true) ||
                        it.exam.contains(query, ignoreCase = true)
            }
        }

        filteredPapers.clear()
        filteredPapers.addAll(filtered)
        adapter.notifyDataSetChanged()
        updateUI()
    }

    private fun openPaper(paper: PreviousPaper) {
        if (paper.pdfUrl.isEmpty()) {
            showError("Paper PDF not available")
            return
        }

        val intent = Intent(this, PdfViewerActivity::class.java)
        intent.putExtra("pdf_url", paper.pdfUrl)
        intent.putExtra("pdf_title", paper.title)
        startActivity(intent)
    }

    private fun downloadPaper(paper: PreviousPaper) {
        // Increment download count
        lifecycleScope.launch {
            FirebaseManager.incrementPaperDownloads(paper.id)
        }

        // Open paper (which will download)
        openPaper(paper)
        showError("Opening Paper...")
    }

    private fun updateUI() {
        binding.tvTotalPapers.text = "${filteredPapers.size} Papers Available"

        if (filteredPapers.isEmpty()) {
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
        binding.rvPreviousPapers.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.rvPreviousPapers.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.headerLayout.startAnimation(fadeIn)
    }
}