package com.roni.class12thagjetnotes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemPdfNoteBinding
import com.roni.class12thagjetnotes.models.firebase.PdfNote

class PdfNotesAdapter(
    private val notes: List<PdfNote>,
    private val onItemClick: (PdfNote) -> Unit,
    private val onDownloadClick: (PdfNote) -> Unit
) : RecyclerView.Adapter<PdfNotesAdapter.PdfNoteViewHolder>() {

    inner class PdfNoteViewHolder(private val binding: ItemPdfNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pdfNote: PdfNote, position: Int) {
            binding.apply {
                // Set data
                tvTitle.text = pdfNote.title
                tvSubject.text = pdfNote.subject
                tvChapter.text = pdfNote.chapter
                tvDescription.text = pdfNote.description
                tvPages.text = "${pdfNote.pages} pages"
                tvSize.text = pdfNote.size
                tvDownloads.text = "${pdfNote.downloads} downloads"

                // Set subject color
                val color = getSubjectColor(pdfNote.subject)
                subjectIndicator.setBackgroundColor(color)

                // Click listeners
                root.setOnClickListener {
                    onItemClick(pdfNote)
                }

                btnDownload.setOnClickListener {
                    onDownloadClick(pdfNote)
                }

                // Animation
                val animation = AnimationUtils.loadAnimation(root.context, R.anim.item_slide_up)
                animation.startOffset = (position * 50).toLong()
                root.startAnimation(animation)
            }
        }

        private fun getSubjectColor(subject: String): Int {
            return when (subject.lowercase()) {
                "mathematics" -> Color.parseColor("#2196F3") // Blue
                "physics" -> Color.parseColor("#FF9800") // Orange
                "chemistry" -> Color.parseColor("#4CAF50") // Green
                "biology" -> Color.parseColor("#E91E63") // Pink
                "english" -> Color.parseColor("#9C27B0") // Purple
                else -> Color.parseColor("#607D8B") // Grey
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfNoteViewHolder {
        val binding = ItemPdfNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PdfNoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PdfNoteViewHolder, position: Int) {
        holder.bind(notes[position], position)
    }

    override fun getItemCount() = notes.size
}