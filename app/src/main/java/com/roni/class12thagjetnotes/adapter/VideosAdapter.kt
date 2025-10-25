package com.roni.class12thagjetnotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemVideoBinding
import com.roni.class12thagjetnotes.models.firebase.Video

class VideosAdapter(
    private val videos: List<Video>,
    private val onItemClick: (Video) -> Unit
) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.apply {
                tvTitle.text = video.title
                tvInstructor.text = video.instructor
                tvDuration.text = video.duration
                tvViews.text = "${video.views} views"
                tvChapter.text = video.chapter

                // Load thumbnail
                Glide.with(root.context)
                    .load(video.thumbnailUrl)
                    .placeholder(R.drawable.placeholder_video)
                    .into(ivThumbnail)

                root.setOnClickListener { onItemClick(video) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount() = videos.size
}