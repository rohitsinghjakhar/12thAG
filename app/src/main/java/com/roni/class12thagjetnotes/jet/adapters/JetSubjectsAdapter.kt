package com.roni.class12thagjetnotes.jet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_jet_subject.view.*

class JetSubjectsAdapter(
    private val subjects: List<Subject>,
    private val onSubjectClick: (Subject) -> Unit
) : RecyclerView.Adapter<JetSubjectsAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardSubject: CardView = itemView.cardSubject
        val tvIcon: TextView = itemView.tvSubjectIcon
        val tvName: TextView = itemView.tvSubjectName
        val tvDescription: TextView = itemView.tvSubjectDescription

        fun bind(subject: Subject) {
            tvIcon.text = subject.icon
            tvName.text = subject.name
            tvDescription.text = subject.description

            // Set card background color with alpha
            try {
                val color = Color.parseColor(subject.colorHex)
                val alpha = 30 // 0-255, lower = more transparent
                val alphaColor = Color.argb(
                    alpha,
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color)
                )
                cardSubject.setCardBackgroundColor(alphaColor)
            } catch (e: Exception) {
                // Use default if color parsing fails
                cardSubject.setCardBackgroundColor(itemView.context.resources.getColor(R.color.card_1, null))
            }

            cardSubject.setOnClickListener {
                onSubjectClick(subject)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_subject, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(subjects[position])
    }

    override fun getItemCount(): Int = subjects.size
}