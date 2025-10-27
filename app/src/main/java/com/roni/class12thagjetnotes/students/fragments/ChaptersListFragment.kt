package com.roni.class12thagjetnotes.students.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.FragmentChaptersListBinding
import com.roni.class12thagjetnotes.students.adapter.ChaptersAdapter
import com.roni.class12thagjetnotes.students.models.Chapter
import com.roni.class12thagjetnotes.students.viewmodels.StudentDeskViewModel
class ChaptersListFragment : Fragment() {

    private var _binding: FragmentChaptersListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StudentDeskViewModel
    private lateinit var chaptersAdapter: ChaptersAdapter
    private var mediumId: String = ""
    private var mediumName: String = ""
    private var subjectName: String = ""

    companion object {
        private const val ARG_MEDIUM_ID = "medium_id"
        private const val ARG_MEDIUM_NAME = "medium_name"
        private const val ARG_SUBJECT_NAME = "subject_name"

        fun newInstance(mediumId: String, mediumName: String, subjectName: String): ChaptersListFragment {
            return ChaptersListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MEDIUM_ID, mediumId)
                    putString(ARG_MEDIUM_NAME, mediumName)
                    putString(ARG_SUBJECT_NAME, subjectName)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mediumId = it.getString(ARG_MEDIUM_ID, "")
            mediumName = it.getString(ARG_MEDIUM_NAME, "")
            subjectName = it.getString(ARG_SUBJECT_NAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChaptersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[StudentDeskViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        if (mediumId.isNotEmpty()) {
            viewModel.loadChapters(mediumId)
        } else {
            showError("Invalid medium ID")
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = subjectName
        binding.toolbar.subtitle = "$mediumName Medium"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        chaptersAdapter = ChaptersAdapter { chapter ->
            navigateToContentTabs(chapter)
        }

        binding.chaptersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chaptersAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.chapters.observe(viewLifecycleOwner) { chapters ->
            if (chapters.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                chaptersAdapter.submitList(chapters)
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

    private fun navigateToContentTabs(chapter: Chapter) {
        val bundle = Bundle().apply {
            putString("chapter_id", chapter.id)
            putString("chapter_name", chapter.name)
        }
        findNavController().navigate(
            R.id.action_chaptersListFragment_to_contentTabsFragment,
            bundle
        )
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.chaptersRecyclerView.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.chaptersRecyclerView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}