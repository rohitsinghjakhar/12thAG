package com.roni.class12thagjetnotes.adapter.study

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.students.models.Chapter

class ChaptersAdapter(
    private val chapters: List<Chapter>,
    private val onItemClick: (Chapter) -> Unit
) : RecyclerView.Adapter<ChaptersAdapter.ChapterViewHolder>() {

    inner class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardChapter)
//        private val tvChapterNumber: TextView = itemView.findViewById(R.id.tvChapterNumber)
//        private val tvChapterName: TextView = itemView.findViewById(R.id.tvChapterName)
//        private val tvDescription: TextView = itemView.findViewById(R.id.tvChapterDescription)
//        private val tvQuizCount: TextView = itemView.findViewById(R.id.tvQuizCount)

        fun bind(chapter: Chapter) {
//            tvChapterNumber.text = "Chapter ${chapter.chapterNumber}"
//            tvChapterName.text = chapter.name
//            tvDescription.text = chapter.description
//            tvQuizCount.text = "${chapter.totalQuizzes} Quizzes"
//
//            // Set color based on chapter number
//            val colors = listOf(
//                "#2196F3", "#FF9800", "#4CAF50", "#E91E63",
//                "#9C27B0", "#00BCD4", "#FF5722", "#607D8B"
//            )
//            val colorIndex = (chapter.chapterNumber - 1) % colors.size
//            cardView.setCardBackgroundColor(Color.parseColor(colors[colorIndex]))
//
//            cardView.setOnClickListener {
//                onItemClick(chapter)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chapter, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bind(chapters[position])
    }

    override fun getItemCount() = chapters.size
}