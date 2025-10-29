package com.roni.class12thagjetnotes.jet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.roni.class12thagjetnotes.jet.models.JetVideo


// Category Adapter
class JetCategoryAdapter(
    private val categories: List<JetCategory>,
    private val onCategoryClick: (JetCategory) -> Unit
) : RecyclerView.Adapter<JetCategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemJetCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size

    inner class CategoryViewHolder(private val binding: ItemJetCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: JetCategory) {
            binding.apply {
                categoryName.text = category.name
                categoryCount.text = "${category.count} items"
                categoryIcon.text = category.icon
                categoryCard.setCardBackgroundColor(Color.parseColor(category.color))

                root.setOnClickListener {
                    onCategoryClick(category)
                }
            }
        }
    }
}

// Video Adapter
class JetVideoAdapter(
    private val videos: List<JetVideo>,
    private val onVideoClick: (JetVideo) -> Unit
) : RecyclerView.Adapter<JetVideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemJetVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount() = videos.size

    inner class VideoViewHolder(private val binding: ItemJetVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: JetVideo) {
            binding.apply {
                videoTitle.text = video.title
                videoDuration.text = video.duration
                videoInstructor.text = video.instructor
                videoCategory.text = video.category

                if (video.thumbnail.isNotEmpty()) {
                    Glide.with(binding.root.context)
                        .load(video.thumbnail)
                        .into(videoThumbnail)
                }

                root.setOnClickListener {
                    onVideoClick(video)
                }
            }
        }
    }
}

// Notes Adapter
class JetNotesAdapter(
    private val notes: List<JetNotes>,
    private val onNoteClick: (JetNotes) -> Unit
) : RecyclerView.Adapter<JetNotesAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemJetNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    inner class NotesViewHolder(private val binding: ItemJetNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: JetNotes) {
            binding.apply {
                notesTitle.text = note.title
                notesSubject.text = note.subject
                notesSize.text = note.size
                notesUploadDate.text = "Uploaded: ${note.uploadDate}"

                if (note.thumbnail.isNotEmpty()) {
                    Glide.with(binding.root.context)
                        .load(note.thumbnail)
                        .into(notesThumbnail)
                }

                root.setOnClickListener {
                    onNoteClick(note)
                }
            }
        }
    }
}

// Previous Year Paper Adapter
class JetPaperAdapter(
    private val papers: List<JetPaper>,
    private val onPaperClick: (JetPaper) -> Unit
) : RecyclerView.Adapter<JetPaperAdapter.PaperViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaperViewHolder {
        val binding = ItemJetPaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaperViewHolder, position: Int) {
        holder.bind(papers[position])
    }

    override fun getItemCount() = papers.size

    inner class PaperViewHolder(private val binding: ItemJetPaperBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(paper: JetPaper) {
            binding.apply {
                paperYear.text = "JET ${paper.year}"
                paperDifficulty.text = paper.difficulty
                paperQuestions.text = "${paper.totalQuestions} Questions"
                paperDate.text = "Uploaded: ${paper.uploadDate}"

                // Set difficulty color
                val difficultyColor = when (paper.difficulty.lowercase()) {
                    "easy" -> Color.parseColor("#4CAF50")
                    "medium" -> Color.parseColor("#FF9800")
                    "hard" -> Color.parseColor("#F44336")
                    else -> Color.parseColor("#2196F3")
                }
                paperDifficulty.setTextColor(difficultyColor)

                root.setOnClickListener {
                    onPaperClick(paper)
                }
            }
        }
    }
}