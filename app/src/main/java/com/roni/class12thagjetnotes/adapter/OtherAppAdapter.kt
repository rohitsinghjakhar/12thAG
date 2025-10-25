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
import com.google.android.material.button.MaterialButton
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.models.OtherApp

class OtherAppAdapter(
    private val apps: List<OtherApp>
) : RecyclerView.Adapter<OtherAppAdapter.OtherAppViewHolder>() {

    inner class OtherAppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardApp: CardView = itemView.findViewById(R.id.cardApp)
        private val ivAppIcon: ImageView = itemView.findViewById(R.id.ivAppIcon)
        private val tvAppName: TextView = itemView.findViewById(R.id.tvAppName)
        private val tvAppDescription: TextView = itemView.findViewById(R.id.tvAppDescription)
        private val tvAppRating: TextView = itemView.findViewById(R.id.tvAppRating)
        private val tvDownloads: TextView = itemView.findViewById(R.id.tvDownloads)
        private val btnDownload: MaterialButton = itemView.findViewById(R.id.btnDownload)

        fun bind(app: OtherApp) {
            ivAppIcon.setImageResource(app.icon)
            tvAppName.text = app.name
            tvAppDescription.text = app.description
            tvAppRating.text = String.format("%.1f", app.rating)
            tvDownloads.text = "• ${app.downloads}"

            btnDownload.setOnClickListener {
                openPlayStore(app.playStoreUrl)
            }

            cardApp.setOnClickListener {
                openPlayStore(app.playStoreUrl)
            }
        }

        private fun openPlayStore(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherAppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_other_app, parent, false)
        return OtherAppViewHolder(view)
    }

    override fun onBindViewHolder(holder: OtherAppViewHolder, position: Int) {
        holder.bind(apps[position])
    }

    override fun getItemCount(): Int = apps.size
}