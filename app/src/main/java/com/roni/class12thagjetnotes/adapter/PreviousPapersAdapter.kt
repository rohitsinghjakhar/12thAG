package com.roni.class12thagjetnotes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemPreviousPaperBinding
import com.roni.class12thagjetnotes.models.firebase.PreviousPaper

class PreviousPapersAdapter(
    private val papers: List<PreviousPaper>,
    private val onItemClick: (PreviousPaper) -> Unit,
    private val onDownloadClick: (PreviousPaper) -> Unit
) : RecyclerView.Adapter<PreviousPapersAdapter.PaperViewHolder>() {

    inner class PaperViewHolder(private val binding: ItemPreviousPaperBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(paper: PreviousPaper, position: Int) {
            binding.apply {
                tvTitle.text = paper.title
                tvSubject.text = paper.subject
                tvYear.text = "Year: ${paper.year}"
                tvExam.text = paper.exam
                tvDownloads.text = "${paper.downloads} downloads"

                // Set subject color
                val color = getSubjectColor(paper.subject)
                subjectIndicator.setBackgroundColor(color)

                // Click listeners
                root.setOnClickListener {
                    onItemClick(paper)
                }

                btnDownload.setOnClickListener {
                    onDownloadClick(paper)
                }

                // Animation
                val animation = AnimationUtils.loadAnimation(root.context, R.anim.item_slide_up)
                animation.startOffset = (position * 50).toLong()
                root.startAnimation(animation)
            }
        }

        private fun getSubjectColor(subject: String): Int {
            return when (subject.lowercase()) {
                "mathematics" -> Color.parseColor("#2196F3")
                "physics" -> Color.parseColor("#FF9800")
                "chemistry" -> Color.parseColor("#4CAF50")
                "biology" -> Color.parseColor("#E91E63")
                "english" -> Color.parseColor("#9C27B0")
                "computer science" -> Color.parseColor("#00BCD4")
                else -> Color.parseColor("#607D8B")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaperViewHolder {
        val binding = ItemPreviousPaperBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaperViewHolder, position: Int) {
        holder.bind(papers[position], position)
    }

    override fun getItemCount() = papers.size
}