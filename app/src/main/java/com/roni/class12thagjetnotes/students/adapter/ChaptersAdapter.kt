package com.roni.class12thagjetnotes.students.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.databinding.ItemChapterBinding
import com.roni.class12thagjetnotes.students.models.Chapter

class ChaptersAdapter(
    private val onChapterClick: (Chapter) -> Unit
) : RecyclerView.Adapter<ChaptersAdapter.ChapterViewHolder>() {

    private var chapters = listOf<Chapter>()

    fun submitList(list: List<Chapter>) {
        chapters = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val binding = ItemChapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bind(chapters[position])
    }

    override fun getItemCount() = chapters.size

    inner class ChapterViewHolder(
        private val binding: ItemChapterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chapter: Chapter) {
            binding.chapterNumber.text = "Chapter ${chapter.chapterNumber}"
            binding.chapterName.text = chapter.name
            binding.chapterDescription.text = chapter.description

            val gradientColors = listOf(
                intArrayOf(0xFF667eea.toInt(), 0xFF764ba2.toInt()),
                intArrayOf(0xFFf093fb.toInt(), 0xFFf5576c.toInt()),
                intArrayOf(0xFF4facfe.toInt(), 0xFF00f2fe.toInt()),
                intArrayOf(0xFF43e97b.toInt(), 0xFF38f9d7.toInt()),
                intArrayOf(0xFFfa709a.toInt(), 0xFFfee140.toInt()),
                intArrayOf(0xFF30cfd0.toInt(), 0xFF330867.toInt())
            )

            val position = adapterPosition % gradientColors.size
            binding.chapterGradientBackground.setGradientColors(gradientColors[position])

            binding.root.setOnClickListener {
                binding.cardView.animate()
                    .scaleX(0.98f)
                    .scaleY(0.98f)
                    .setDuration(100)
                    .withEndAction {
                        binding.cardView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                        onChapterClick(chapter)
                    }
                    .start()
            }
        }
    }
}