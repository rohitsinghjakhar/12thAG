package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_jet_update.view.*

class JetUpdatesAdapter(
    private val updates: List<JetUpdate>,
    private val onUpdateClick: (JetUpdate) -> Unit
) : RecyclerView.Adapter<JetUpdatesAdapter.UpdateViewHolder>() {

    inner class UpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardUpdate: CardView = itemView.cardUpdate
        val ivUpdateImage: ImageView = itemView.ivUpdateImage
        val tvUpdateTitle: TextView = itemView.tvUpdateTitle
        val tvUpdateDate: TextView = itemView.tvUpdateDate
        val tvUpdateType: TextView = itemView.tvUpdateType
        val tvImportantBadge: TextView = itemView.tvImportantBadge

        fun bind(update: JetUpdate) {
            tvUpdateTitle.text = update.title
            tvUpdateDate.text = update.date
            tvUpdateType.text = getTypeDisplayName(update.type)

            // Show/hide important badge
            tvImportantBadge.visibility = if (update.isImportant) View.VISIBLE else View.GONE

            // Load image if available
            if (update.imageUrl.isNotEmpty()) {
                ivUpdateImage.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(update.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(ivUpdateImage)
            } else {
                ivUpdateImage.visibility = View.GONE
            }

            // Set card color based on type
            val cardColor = when (update.type) {
                "exam_date" -> R.color.card_1
                "result" -> R.color.card_2
                "announcement" -> R.color.card_3
                "news" -> R.color.card_4
                else -> R.color.card_5
            }
            cardUpdate.setCardBackgroundColor(
                itemView.context.resources.getColor(cardColor, null)
            )

            // Click listener
            cardUpdate.setOnClickListener {
                onUpdateClick(update)
            }
        }

        private fun getTypeDisplayName(type: String): String {
            return when (type) {
                "exam_date" -> "📅 Exam Date"
                "result" -> "📊 Result"
                "announcement" -> "📢 Announcement"
                "news" -> "📰 News"
                "syllabus_update" -> "📚 Syllabus"
                "admission" -> "🎓 Admission"
                else -> "ℹ️ Update"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_update, parent, false)
        return UpdateViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        holder.bind(updates[position])
    }

    override fun getItemCount(): Int = updates.size
}