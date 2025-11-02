package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.jet.models.JetTip
import kotlinx.android.synthetic.main.item_jet_tip.view.* // You need to create item_jet_tip.xml

class JetTipsAdapter(
    private val tipsList: List<JetTip>,
    private val onClick: (JetTip) -> Unit
) : RecyclerView.Adapter<JetTipsAdapter.TipViewHolder>() {

    inner class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tip: JetTip) {
            itemView.tvTipTitle.text = tip.title
            itemView.tvTipCategory.text = tip.category

            if (tip.isPremium) {
                itemView.ivPremiumIcon.visibility = View.VISIBLE
            } else {
                itemView.ivPremiumIcon.visibility = View.GONE
            }

            itemView.setOnClickListener { onClick(tip) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_tip, parent, false)
        return TipViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.bind(tipsList[position])
    }

    override fun getItemCount(): Int = tipsList.size
}