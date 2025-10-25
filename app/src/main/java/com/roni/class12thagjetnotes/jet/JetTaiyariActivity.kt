package com.roni.class12thagjetnotes.jet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.roni.class12thagjetnotes.databinding.ActivityJetTaiyariBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.quiz.QuizPlayActivity
import com.roni.class12thagjetnotes.viewer.PdfViewerActivity

class JetTaiyariActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJetTaiyariBinding
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var jetContentAdapter: JetContentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJetTaiyariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        initializeRecyclerView()
        setupFirebase()
        loadJetContent()
        setupTabLayout()
    }

    private fun setupToolbar() {
        binding.toolbar.title = "JET Taiyari"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initializeRecyclerView() {
        jetContentAdapter = JetContentAdapter { content ->
            when (content.type) {
                "syllabus" -> openPdfViewer(content.title, content.url)
                "mock_test" -> openMockTest(content)
                "important_dates" -> showImportantDates(content)
                "preparation_tips" -> showPreparationTips(content)
                "study_material" -> openPdfViewer(content.title, content.url)
            }
        }

        binding.contentRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@JetTaiyariActivity)
            adapter = jetContentAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupFirebase() {
        firebaseManager = FirebaseManager()
    }

    private fun loadJetContent() {
        showLoading(true)

        firebaseManager.getJetContent { contentList, error ->
            showLoading(false)

            if (error != null) {
                showError("Failed to load JET content: ${error.message}")
                return@getJetContent
            }

            if (contentList.isNullOrEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
                jetContentAdapter.submitList(contentList)
            }
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> filterContent("syllabus")
                    1 -> filterContent("mock_test")
                    2 -> filterContent("important_dates")
                    3 -> filterContent("preparation_tips")
                    4 -> filterContent("study_material")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun filterContent(type: String) {
        // This would filter the existing list by type
        // For now, we'll reload with filter
        showLoading(true)

        firebaseManager.getJetContentByType(type) { contentList, error ->
            showLoading(false)

            if (error != null) {
                showError("Failed to load content: ${error.message}")
                return@getJetContentByType
            }

            jetContentAdapter.submitList(contentList ?: emptyList())
        }
    }

    private fun openPdfViewer(title: String, url: String) {
        val intent = Intent(this, PdfViewerActivity::class.java).apply {
            putExtra("pdf_title", title)
            putExtra("pdf_url", url)
        }
        startActivity(intent)
    }

    private fun openMockTest(content: JetContent) {
        // Navigate to quiz play activity with mock test
        val intent = Intent(this, QuizPlayActivity::class.java).apply {
            putExtra("quiz_id", content.id)
            putExtra("quiz_title", content.title)
            putExtra("is_mock_test", true)
        }
        startActivity(intent)
    }

    private fun showImportantDates(content: JetContent) {
        // Show important dates in a dialog or new activity
        MaterialAlertDialogBuilder(this)
            .setTitle(content.title)
            .setMessage(content.description)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showPreparationTips(content: JetContent) {
        // Show preparation tips in a dialog
        MaterialAlertDialogBuilder(this)
            .setTitle(content.title)
            .setMessage(content.description)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.contentRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(show: Boolean) {
        binding.emptyStateLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.contentRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.emptyStateText.text = message
        showEmptyState(true)
    }
}