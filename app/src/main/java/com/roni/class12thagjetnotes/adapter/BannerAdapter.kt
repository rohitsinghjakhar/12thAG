package com.roni.class12thagjetnotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.storage.FirebaseStorage
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.models.Banner


class BannerAdapter(
    private val banners: List<Banner>,
    private val onBannerClick: (Banner) -> Unit = {}
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBanner: ImageView = itemView.findViewById(R.id.ivBanner)
        private val tvBannerTitle: TextView = itemView.findViewById(R.id.tvBannerTitle)
        private val tvBannerSubtitle: TextView = itemView.findViewById(R.id.tvBannerSubtitle)
        private val shimmerLayout: ShimmerFrameLayout = itemView.findViewById(R.id.shimmerLayout)

        fun bind(banner: Banner) {
            // Set title and subtitle
            if (banner.title.isNotEmpty()) {
                tvBannerTitle.text = banner.title
                tvBannerTitle.visibility = View.VISIBLE
            }

            if (banner.subtitle.isNotEmpty()) {
                tvBannerSubtitle.text = banner.subtitle
                tvBannerSubtitle.visibility = View.VISIBLE
            }

            // Load image
            if (banner.imageUrl.isNotEmpty()) {
                shimmerLayout.startShimmer()
                loadBannerImage(banner.imageUrl)
            } else {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                ivBanner.visibility = View.VISIBLE
                ivBanner.setImageResource(R.drawable.gradient_background)
            }

            itemView.setOnClickListener {
                onBannerClick(banner)
            }
        }

        private fun loadBannerImage(imageUrl: String) {
            if (imageUrl.startsWith("gs://") || imageUrl.startsWith("banners/")) {
                val storageRef = if (imageUrl.startsWith("gs://")) {
                    FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
                } else {
                    FirebaseStorage.getInstance().reference.child(imageUrl)
                }

                storageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        Glide.with(itemView.context)
                            .load(uri)
                            .into(ivBanner)

                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                        ivBanner.visibility = View.VISIBLE
                    }
                    .addOnFailureListener {
                        showDefaultImage()
                    }
            } else {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .into(ivBanner)

                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                ivBanner.visibility = View.VISIBLE
            }
        }

        private fun showDefaultImage() {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            ivBanner.visibility = View.VISIBLE
            ivBanner.setImageResource(R.drawable.gradient_background)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(banners[position])
    }

    override fun getItemCount(): Int = banners.size
}