package com.roni.class12thagjetnotes.students.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.FragmentContentTabsBinding
import com.roni.class12thagjetnotes.students.adapter.ContentPagerAdapter

class ContentTabsFragment : Fragment() {
    private var _binding: FragmentContentTabsBinding? = null
    private val binding get() = _binding!!
    private lateinit var chapterId: String
    private lateinit var chapterName: String

    companion object {
        private const val ARG_CHAPTER_ID = "chapter_id"
        private const val ARG_CHAPTER_NAME = "chapter_name"

        fun newInstance(chapterId: String, chapterName: String): ContentTabsFragment {
            return ContentTabsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CHAPTER_ID, chapterId)
                    putString(ARG_CHAPTER_NAME, chapterName)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chapterId = it.getString(ARG_CHAPTER_ID) ?: run {
                Log.e("ContentTabs", "Missing chapterId!")
                requireActivity().onBackPressed()
                return
            }
            chapterName = it.getString(ARG_CHAPTER_NAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentTabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupViewPager()
    }

    private fun setupToolbar() {
        binding.toolbar.title = chapterName
        binding.toolbar.subtitle = "Study Materials"
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupViewPager() {
        val adapter = ContentPagerAdapter(this, chapterId)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Notes"
                1 -> "Books"
                2 -> "Quizzes"
                3 -> "Videos"
                4 -> "Mind Maps"
                else -> ""
            }
            tab.icon = when (position) {
                0 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_notes)
                1 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_books)
                2 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_quizzes)
                3 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_video)
                4 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_mindmap)
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}