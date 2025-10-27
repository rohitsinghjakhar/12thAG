package com.roni.class12thagjetnotes.students.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemContentBinding
import com.roni.class12thagjetnotes.students.models.Content

class ContentAdapter(
    private val onContentClick: (Content) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    private var contentList = listOf<Content>()

    fun submitList(list: List<Content>) {
        contentList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val binding = ItemContentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(contentList[position])
    }

    override fun getItemCount() = contentList.size

    inner class ContentViewHolder(
        private val binding: ItemContentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: Content) {
            binding.contentTitle.text = content.title
            binding.contentDescription.text = content.description

            // Load thumbnail based on content type
            when (content.type) {
                "videos" -> {
                    if (content.thumbnailUrl.isNotEmpty()) {
                        Glide.with(context)
                            .load(content.thumbnailUrl)
                            .placeholder(R.drawable.ic_video_placeholder)
                            .error(R.drawable.ic_video_placeholder)
                            .centerCrop()
                            .into(binding.contentThumbnail)
                    } else {
                        binding.contentThumbnail.setImageResource(R.drawable.ic_video_placeholder)
                    }
                }
                else -> {
                    binding.contentThumbnail.setImageResource(getContentTypePlaceholder(content.type))
                }
            }

            // Set content type icon
            binding.contentTypeIcon.setImageResource(getContentTypeIcon(content.type))

            // Handle click
            binding.root.setOnClickListener {
                onContentClick(content)
            }
        }

        private fun getContentTypePlaceholder(type: String): Int {
            return when (type) {
                "notes" -> R.drawable.ic_notes_placeholder
                "books" -> R.drawable.ic_books_placeholder
                "quizzes" -> R.drawable.ic_quiz_placeholder
                "videos" -> R.drawable.ic_video_placeholder
                "mindmaps" -> R.drawable.ic_content_placeholder
                else -> R.drawable.ic_content_placeholder
            }
        }

        private fun getContentTypeIcon(type: String): Int {
            return when (type) {
                "notes" -> R.drawable.ic_notes
                "books" -> R.drawable.ic_books
                "quizzes" -> R.drawable.ic_quizzes
                "videos" -> R.drawable.ic_video
                "mindmaps" -> R.drawable.ic_mindmap
                else -> R.drawable.ic_content_placeholder
            }
        }
    }
}