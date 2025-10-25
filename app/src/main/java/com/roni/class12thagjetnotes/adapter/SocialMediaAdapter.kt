package com.roni.class12thagjetnotes.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.models.SocialMedia


class SocialMediaAdapter(
    private val socialMediaList: List<SocialMedia>
) : RecyclerView.Adapter<SocialMediaAdapter.SocialMediaViewHolder>() {

    inner class SocialMediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardSocialMedia: CardView = itemView.findViewById(R.id.cardSocialMedia)
        private val ivSocialIcon: ImageView = itemView.findViewById(R.id.ivSocialIcon)
        private val tvSocialName: TextView = itemView.findViewById(R.id.tvSocialName)

        fun bind(socialMedia: SocialMedia) {
            //ivSocialIcon.setImageResource(socialMedia.icon)
            tvSocialName.text = socialMedia.name
            cardSocialMedia.setCardBackgroundColor(socialMedia.backgroundColor)

            itemView.setOnClickListener {
                openSocialMediaLink(socialMedia.url)
            }
        }

        private fun openSocialMediaLink(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialMediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_social_media, parent, false)
        return SocialMediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocialMediaViewHolder, position: Int) {
        holder.bind(socialMediaList[position])
    }

    override fun getItemCount(): Int = socialMediaList.size
}