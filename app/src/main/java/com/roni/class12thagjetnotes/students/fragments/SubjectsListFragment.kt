package com.roni.class12thagjetnotes.students.fragments

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
import com.roni.class12thagjetnotes.adapter.study.SubjectsAdapter
import com.roni.class12thagjetnotes.databinding.FragmentSubjectsListBinding
import com.roni.class12thagjetnotes.models.firebase.Subject
import com.roni.class12thagjetnotes.students.viewmodels.StudentDeskViewModel
import com.uhadawnbells.uha.fragments.MediumSelectionFragment

class SubjectsListFragment : Fragment() {

    private var _binding: FragmentSubjectsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StudentDeskViewModel
    private lateinit var subjectsAdapter: SubjectsAdapter

    private var classId: String = ""
    private var className: String = ""

    companion object {
        private const val TAG = "SubjectsListFragment"
        private const val ARG_CLASS_ID = "class_id"
        private const val ARG_CLASS_NAME = "class_name"

        fun newInstance(classId: String, className: String): SubjectsListFragment {
            return SubjectsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CLASS_ID, classId)
                    putString(ARG_CLASS_NAME, className)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            classId = it.getString(ARG_CLASS_ID, "")
            className = it.getString(ARG_CLASS_NAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[StudentDeskViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        if (classId.isNotEmpty()) {
            viewModel.loadSubjects(classId)
        } else {
            showErrorAndGoBack("Invalid class selected")
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = "$className - Subjects"
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        subjectsAdapter = SubjectsAdapter() { subject ->
            navigateToMediumSelection(subject)
        }

        binding.subjectsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = subjectsAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.subjects.observe(viewLifecycleOwner) { subjects ->
            Log.d(TAG, "Received ${subjects.size} subjects")
            if (subjects.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                subjectsAdapter = SubjectsAdapter { subject ->
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToMediumSelection(subject: Subject) {
        Log.d(TAG, "Navigating to medium selection for subject: ${subject.name}")

        val fragment = MediumSelectionFragment.newInstance(subject.id, subject.name)

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
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.subjectsRecyclerView.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.subjectsRecyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}