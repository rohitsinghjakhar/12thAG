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
import com.roni.class12thagjetnotes.adapter.PdfNotesAdapter
import com.roni.class12thagjetnotes.databinding.ActivityPdfNotesBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.models.firebase.PdfNote
import com.roni.class12thagjetnotes.viewer.PdfViewerActivity
import kotlinx.coroutines.launch

class PdfNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfNotesBinding
    private val allNotes = mutableListOf<PdfNote>()
    private val filteredNotes = mutableListOf<PdfNote>()
    private lateinit var adapter: PdfNotesAdapter
    private var selectedSubject: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedSubject = intent.getStringExtra("subject_name") ?: "All"

        setupUI()
        setupRecyclerView()
        loadPdfNotes()
        startAnimations()
    }

    private fun setupUI() {
        binding.tvTitle.text = if (selectedSubject != "All") {
            "$selectedSubject PDF Notes"
        } else {
            "All PDF Notes"
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterNotes(newText ?: "")
                return true
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            loadPdfNotes()
        }
    }

    private fun setupRecyclerView() {
        adapter = PdfNotesAdapter(
            notes = filteredNotes,
            onItemClick = { note ->
                openPdf(note)
            },
            onDownloadClick = { note ->
                downloadPdf(note)
            }
        )

        binding.rvPdfNotes.layoutManager = LinearLayoutManager(this)
        binding.rvPdfNotes.adapter = adapter
    }

    private fun loadPdfNotes() {
        showLoading(true)

        lifecycleScope.launch {
            val result = FirebaseManager.getPdfNotes(
                if (selectedSubject != "All") selectedSubject else null
            )

            result.onSuccess { notes ->
                allNotes.clear()
                allNotes.addAll(notes)
                filteredNotes.clear()
                filteredNotes.addAll(notes)
                adapter.notifyDataSetChanged()
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                updateUI()
            }.onFailure { error ->
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                showEmptyState()
                showError(error.message ?: "Failed to load PDF notes")
            }
        }
    }

    private fun filterNotes(query: String) {
        val filtered = if (query.isEmpty()) {
            allNotes
        } else {
            allNotes.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.chapter.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }

        filteredNotes.clear()
        filteredNotes.addAll(filtered)
        adapter.notifyDataSetChanged()
        updateUI()
    }

    private fun openPdf(note: PdfNote) {
        if (note.pdfUrl.isEmpty()) {
            showError("PDF URL not available")
            return
        }

        val intent = Intent(this, PdfViewerActivity::class.java)
        intent.putExtra("pdf_url", note.pdfUrl)
        intent.putExtra("pdf_title", note.title)
        startActivity(intent)
    }

    private fun downloadPdf(note: PdfNote) {
        // Increment download count
        lifecycleScope.launch {
            FirebaseManager.incrementPdfDownloads(note.id)
        }

        // Open PDF viewer (which will download)
        openPdf(note)
        showError("Opening PDF...")
    }

    private fun updateUI() {
        binding.tvTotalNotes.text = "${filteredNotes.size} Notes Available"

        if (filteredNotes.isEmpty()) {
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
        binding.rvPdfNotes.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.rvPdfNotes.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.headerLayout.startAnimation(fadeIn)
    }
}