package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.jet.models.JetMindMap
// Import Glide or Picasso
// import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_jet_mind_map.view.* // You need to create item_jet_mind_map.xml

class JetMindMapAdapter(
    private val mindMapList: List<JetMindMap>,
    private val onClick: (JetMindMap) -> Unit
) : RecyclerView.Adapter<JetMindMapAdapter.MindMapViewHolder>() {

    inner class MindMapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(map: JetMindMap) {
            itemView.tvMindMapTitle.text = map.title

            if (map.isPremium) {
                itemView.ivPremiumIcon.visibility = View.VISIBLE
            } else {
                itemView.ivPremiumIcon.visibility = View.GONE
            }

            // Load image (using Glide example)
            // Glide.with(itemView.context)
            //    .load(map.imageUrl)
            //    .placeholder(R.drawable.placeholder_image)
            //    .into(itemView.ivMindMapImage)

            itemView.setOnClickListener { onClick(map) }
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