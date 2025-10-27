package com.roni.class12thagjetnotes.students.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.roni.class12thagjetnotes.students.fragments.ContentListFragment


class ContentPagerAdapter(
    fragment: Fragment,
    private val chapterId: String,
    private val chapterName: String = ""
) : FragmentStateAdapter(fragment) {

    private val contentTypes = listOf("notes", "books", "quizzes", "videos", "mindmaps")
    private val contentTitles = listOf("Notes", "Books", "Quizzes", "Videos", "Mind Maps")

    override fun getItemCount(): Int = contentTypes.size

    override fun createFragment(position: Int): Fragment {
        return ContentListFragment.newInstance(
            chapterId = chapterId,
            contentType = contentTypes[position],
            chapterName = chapterName
        )
    }

    fun getPageTitle(position: Int): String {
        return contentTitles.getOrNull(position) ?: "Content"
    }
}