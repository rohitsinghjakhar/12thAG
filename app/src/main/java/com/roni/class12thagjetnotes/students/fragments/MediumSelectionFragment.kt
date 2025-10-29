// File: app/src/main/java/com/roni/class12thagjetnotes/students/fragments/MediumSelectionFragment.kt
package com.uhadawnbells.uha.fragments // Keep original package if intended

// ... (imports) ...
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.FragmentMediumSelectionBinding // Ensure this binding exists
import com.roni.class12thagjetnotes.students.adapter.MediumAdapter
import com.roni.class12thagjetnotes.students.fragments.ChaptersListFragment
import com.roni.class12thagjetnotes.students.models.Medium
import com.roni.class12thagjetnotes.students.viewmodels.StudentDeskViewModel


class MediumSelectionFragment : Fragment() {
    // ... (binding, viewModel, adapter declarations) ...
    private var _binding: FragmentMediumSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StudentDeskViewModel
    private lateinit var mediumAdapter: MediumAdapter

    private var subjectId: String = ""
    private var subjectName: String = ""

    // ... (companion object, onCreate) ...
    companion object {
        private const val TAG = "MediumSelectionFragment"
        private const val ARG_SUBJECT_ID = "subject_id"
        private const val ARG_SUBJECT_NAME = "subject_name"

        fun newInstance(subjectId: String, subjectName: String): MediumSelectionFragment {
            return MediumSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SUBJECT_ID, subjectId)
                    putString(ARG_SUBJECT_NAME, subjectName)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subjectId = it.getString(ARG_SUBJECT_ID, "")
            subjectName = it.getString(ARG_SUBJECT_NAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediumSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[StudentDeskViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        if (subjectId.isNotEmpty()) {
            viewModel.loadMediums(subjectId)
        } else {
            showErrorAndGoBack("Invalid subject selected")
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = "$subjectName - Select Medium"
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        mediumAdapter = MediumAdapter { medium ->
            navigateToChapters(medium)
        }

        binding.mediumRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mediumAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.mediums.observe(viewLifecycleOwner) { mediums ->
            Log.d(TAG, "Received ${mediums.size} mediums")
            if (mediums.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                mediumAdapter.submitList(mediums)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            // --- Fix: Access visibility via binding ---
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToChapters(medium: Medium) {
        Log.d(TAG, "Navigating to chapters for medium: ${medium.name}")

        val fragment = ChaptersListFragment.newInstance(
            mediumId = medium.id,
            mediumName = medium.name,
            subjectName = subjectName
        )

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showErrorAndGoBack(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.root.visibility = View.VISIBLE
        binding.mediumRecyclerView.visibility = View.GONE
        binding.emptyStateLayout.emptyStateText.text = "No mediums found for this subject."
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.root.visibility = View.GONE
        binding.mediumRecyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}