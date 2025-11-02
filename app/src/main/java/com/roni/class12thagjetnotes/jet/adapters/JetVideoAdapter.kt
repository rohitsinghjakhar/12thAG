package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.jet.models.JetVideo
// Import Glide or Picasso
// import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_jet_video.view.*

class JetVideoAdapter(
    private val videoList: List<JetVideo>,
    private val onClick: (JetVideo) -> Unit
) : RecyclerView.Adapter<JetVideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(video: JetVideo) {
            itemView.tvVideoTitle.text = video.title
            itemView.tvVideoDuration.text = video.duration

            if (video.isPremium) {
                itemView.ivPremiumIcon.visibility = View.VISIBLE
            } else {
                itemView.ivPremiumIcon.visibility = View.GONE
            }

            // Load thumbnail (using Glide example)
            val thumbnailUrl = "https://img.youtube.com/vi/${video.youtubeId}/0.jpg"
            // Glide.with(itemView.context)
            //    .load(thumbnailUrl)
            //    .placeholder(R.drawable.placeholder_video)
            //    .into(itemView.ivVideoThumbnail)

            itemView.setOnClickListener { onClick(video) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_video, parent, false) // You need to create item_jet_video.xml
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    override fun getItemCount(): Int = videoList.size
}