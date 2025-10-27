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
import com.roni.class12thagjetnotes.databinding.FragmentClassesListBinding
import com.roni.class12thagjetnotes.students.adapter.ClassesAdapter
import com.roni.class12thagjetnotes.students.models.ClassModel
import com.roni.class12thagjetnotes.students.viewmodels.StudentDeskViewModel

class ClassesListFragment : Fragment() {

    private var _binding: FragmentClassesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StudentDeskViewModel
    private lateinit var classesAdapter: ClassesAdapter

    companion object {
        private const val TAG = "ClassesListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get shared ViewModel
        viewModel = ViewModelProvider(requireActivity())[StudentDeskViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        // Load classes
        viewModel.loadClasses()
    }

    private fun setupToolbar() {
        binding.toolbar.title = "Select Your Class"
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        classesAdapter = ClassesAdapter { classModel ->
            navigateToSubjects(classModel)
        }

        binding.classesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = classesAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.classes.observe(viewLifecycleOwner) { classes ->
            Log.d(TAG, "Received ${classes.size} classes from Firebase")
            if (classes.isEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                classesAdapter.submitList(classes)
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

    private fun navigateToSubjects(classModel: ClassModel) {
        Log.d(TAG, "Navigating to subjects for class: ${classModel.name}")

        val fragment = SubjectsListFragment.newInstance(classModel.id, classModel.name)

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

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.classesRecyclerView.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.classesRecyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}