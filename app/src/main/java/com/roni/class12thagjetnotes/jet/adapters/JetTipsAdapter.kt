package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.jet.models.JetTip

class JetTipsAdapter(
    private val tipsList: List<JetTip>,
    private val onTipClick: (JetTip) -> Unit,
    private val onLikeClick: (JetTip) -> Unit
) : RecyclerView.Adapter<JetTipsAdapter.TipViewHolder>() {

    inner class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTip: CardView = itemView.cardTip
        val tvTitle: TextView = itemView.tvTipTitle
        val tvContent: TextView = itemView.tvTipContent
        val tvCategory: TextView = itemView.tvTipCategory
        val tvAuthor: TextView = itemView.tvTipAuthor
        val tvLikes: TextView = itemView.tvTipLikes
        val ivLike: ImageView = itemView.ivTipLike
        val tvVerifiedBadge: TextView = itemView.tvVerifiedBadge

        fun bind(tip: JetTip) {
            tvTitle.text = tip.title
            tvContent.text = tip.content
            tvCategory.text = tip.category
            tvAuthor.text = "By ${tip.author}"
            tvLikes.text = tip.likes.toString()

            tvVerifiedBadge.visibility = if (tip.isVerified) View.VISIBLE else View.GONE

            val categoryColor = when (tip.category) {
                "Study Tips" -> R.color.primary
                "Exam Strategy" -> R.color.success
                "Time Management" -> R.color.warning
                "Revision Tips" -> R.color.info
                else -> R.color.text_secondary
            }
            tvCategory.setTextColor(itemView.context.resources.getColor(categoryColor, null))

            cardTip.setOnClickListener {
                onTipClick(tip)
            }

            ivLike.setOnClickListener {
                onLikeClick(tip)
            }
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