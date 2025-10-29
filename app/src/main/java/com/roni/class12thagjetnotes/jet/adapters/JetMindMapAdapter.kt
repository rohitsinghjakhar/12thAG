package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.roni.class12thagjetnotes.jet.models.JetMindMap

class JetMindMapAdapter(
    private val mindMapList: List<JetMindMap>,
    private val onMindMapClick: (JetMindMap) -> Unit
) : RecyclerView.Adapter<JetMindMapAdapter.MindMapViewHolder>() {

    inner class MindMapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardMindMap: CardView = itemView.cardMindMap
        val ivMindMap: ImageView = itemView.ivMindMapImage
        val tvTitle: TextView = itemView.tvMindMapTitle
        val tvSubject: TextView = itemView.tvMindMapSubject
        val tvType: TextView = itemView.tvMindMapType
        val tvViews: TextView = itemView.tvMindMapViews
        val tvPremiumBadge: TextView = itemView.tvPremiumBadge

        fun bind(mindMap: JetMindMap) {
            tvTitle.text = mindMap.title
            tvSubject.text = mindMap.subject
            tvType.text = mindMap.type
            tvViews.text = "${mindMap.viewCount} views"

            tvPremiumBadge.visibility = if (mindMap.isPremium) View.VISIBLE else View.GONE

            if (mindMap.thumbnailUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(mindMap.thumbnailUrl)
                    .centerCrop()
                    .placeholder(R.drawable.mind_map_placeholder)
                    .into(ivMindMap)
            } else if (mindMap.imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(mindMap.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.mind_map_placeholder)
                    .into(ivMindMap)
            } else {
                ivMindMap.setImageResource(R.drawable.mind_map_placeholder)
            }

            cardMindMap.setOnClickListener {
                onMindMapClick(mindMap)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MindMapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_mind_map, parent, false)
        return MindMapViewHolder(view)
    }

    override fun onBindViewHolder(holder: MindMapViewHolder, position: Int) {
        holder.bind(mindMapList[position])
    }

    override fun getItemCount(): Int = mindMapList.size
}