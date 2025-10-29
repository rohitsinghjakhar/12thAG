package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.roni.class12thagjetnotes.jet.models.JetVideo
import kotlinx.android.synthetic.main.item_jet_video.view.*

class JetVideoAdapter(
    private val videoList: List<JetVideo>,
    private val onVideoClick: (JetVideo) -> Unit
) : RecyclerView.Adapter<JetVideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardVideo: CardView = itemView.cardVideo
        val ivThumbnail: ImageView = itemView.ivVideoThumbnail
        val tvTitle: TextView = itemView.tvVideoTitle
        val tvInstructor: TextView = itemView.tvVideoInstructor
        val tvDuration: TextView = itemView.tvVideoDuration
        val tvViews: TextView = itemView.tvVideoViews
        val tvSubject: TextView = itemView.tvVideoSubject
        val tvNewBadge: TextView = itemView.tvNewBadge
        val tvPremiumBadge: TextView = itemView.tvPremiumBadge
        val ivPlayIcon: ImageView = itemView.ivPlayIcon

        fun bind(video: JetVideo) {
            tvTitle.text = video.title
            tvInstructor.text = video.instructor
            tvDuration.text = video.duration
            tvViews.text = "${video.viewCount} views"
            tvSubject.text = video.subject

            // Show/hide badges
            tvNewBadge.visibility = if (video.isNew) View.VISIBLE else View.GONE
            tvPremiumBadge.visibility = if (video.isPremium) View.VISIBLE else View.GONE

            // Load thumbnail
            if (video.thumbnailUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(video.thumbnailUrl)
                    .centerCrop()
                    .placeholder(R.drawable.video_placeholder)
                    .into(ivThumbnail)
            } else if (video.youtubeId.isNotEmpty()) {
                val thumbnailUrl = "https://img.youtube.com/vi/${video.youtubeId}/hqdefault.jpg"
                Glide.with(itemView.context)
                    .load(thumbnailUrl)
                    .centerCrop()
                    .placeholder(R.drawable.video_placeholder)
                    .into(ivThumbnail)
            } else {
                ivThumbnail.setImageResource(R.drawable.video_placeholder)
            }

            // Click listener
            cardVideo.setOnClickListener {
                onVideoClick(video)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    override fun getItemCount(): Int = videoList.size
}