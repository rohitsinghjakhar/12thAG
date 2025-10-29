package com.roni.class12thagjetnotes.students.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.roni.class12thagjetnotes.databinding.FragmentContentListBinding
import com.roni.class12thagjetnotes.students.adapter.ContentAdapter
import com.roni.class12thagjetnotes.students.models.Content
import com.roni.class12thagjetnotes.students.viewers.PdfViewerActivity
import com.roni.class12thagjetnotes.students.viewers.VideoPlayerActivity
import com.roni.class12thagjetnotes.students.quiz.QuizTakeActivity
import com.roni.class12thagjetnotes.students.viewmodels.StudentDeskViewModel

class ContentListFragment : Fragment() {

    private var _binding: FragmentContentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StudentDeskViewModel
    private lateinit var contentAdapter: ContentAdapter

    private lateinit var chapterId: String
    private lateinit var contentType: String
    private lateinit var chapterName: String

    companion object {
        private const val TAG = "ContentListFragment"
        private const val ARG_CHAPTER_ID = "chapter_id"
        private const val ARG_CONTENT_TYPE = "content_type"
        private const val ARG_CHAPTER_NAME = "chapter_name"

        fun newInstance(chapterId: String, contentType: String, chapterName: String = ""): ContentListFragment {
            return ContentListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CHAPTER_ID, chapterId)
                    putString(ARG_CONTENT_TYPE, contentType)
                    putString(ARG_CHAPTER_NAME, chapterName)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chapterId = it.getString(ARG_CHAPTER_ID) ?: ""
            contentType = it.getString(ARG_CONTENT_TYPE) ?: ""
            chapterName = it.getString(ARG_CHAPTER_NAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[StudentDeskViewModel::class.java]

        setupRecyclerView()

        if (chapterId.isNotEmpty() && contentType.isNotEmpty()) {
            observeContentForThisTab()
        } else {
            Log.e(TAG, "ChapterId or ContentType is empty!")
            showError("Could not load content.")
        }
    }

    private fun setupRecyclerView() {
        contentAdapter = ContentAdapter({ content ->
            handleContentClick(content)
        }, requireContext())

        binding.contentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contentAdapter
            setHasFixedSize(true)
        }
    }

    // ✅ FIX: Observe content specific to this tab/fragment
    private fun observeContentForThisTab() {
        // Get LiveData specific to this chapter + content type combination
        val contentLiveData = viewModel.getContentLiveData(chapterId, contentType)

        contentLiveData.observe(viewLifecycleOwner) { contentList ->
            Log.d(TAG, "Received ${contentList.size} items for type $contentType in chapter $chapterId")
            if (contentList.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                contentAdapter.submitList(contentList)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showError(it)
            }
        }
    }

    private fun handleContentClick(content: Content) {
        Log.d(TAG, "Content clicked: ${content.title} (Type: ${content.type})")
        when (content.type) {
            "notes", "books", "mindmaps" -> {
                val intent = Intent(activity, PdfViewerActivity::class.java).apply {
                    putExtra("pdf_url", content.url)
                    putExtra("title", content.title)
                }
                startActivity(intent)
            }
            "videos" -> {
                val intent = Intent(activity, VideoPlayerActivity::class.java).apply {
                    putExtra("video_url", content.url)
                    putExtra("title", content.title)
                }
                startActivity(intent)
            }
            "quizzes" -> {
                val intent = Intent(activity, QuizTakeActivity::class.java).apply {
                    putExtra("quiz_id", content.id)
                    putExtra("quiz_title", content.title)
                }
                startActivity(intent)
            }
            else -> {
                showError("Unsupported content type: ${content.type}")
            }
        }
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.root.visibility = View.VISIBLE
        binding.contentRecyclerView.visibility = View.GONE
        binding.emptyStateLayout.emptyStateText.text = "No $contentType found for this chapter."
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.root.visibility = View.GONE
        binding.contentRecyclerView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}