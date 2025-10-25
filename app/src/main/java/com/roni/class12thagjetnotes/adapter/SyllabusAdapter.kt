package com.roni.class12thagjetnotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.models.firebase.Syllabus

class SyllabusAdapter(
    private val syllabusList: List<Syllabus>,
    private val onItemClick: (Syllabus) -> Unit
) : RecyclerView.Adapter<SyllabusAdapter.SyllabusViewHolder>() {

    inner class SyllabusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardSyllabus)
//        private val tvTitle: TextView = itemView.findViewById(R.id.tvSyllabusTitle)
//        private val tvSubject: TextView = itemView.findViewById(R.id.tvSyllabusSubject)
//        private val tvDescription: TextView = itemView.findViewById(R.id.tvSyllabusDescription)

        fun bind(syllabus: Syllabus) {
//            tvTitle.text = syllabus.title
//            tvSubject.text = syllabus.subject
//            tvDescription.text = syllabus.description

            cardView.setOnClickListener {
                onItemClick(syllabus)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyllabusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_syllabus, parent, false)
        return SyllabusViewHolder(view)
    }

    override fun onBindViewHolder(holder: SyllabusViewHolder, position: Int) {
        holder.bind(syllabusList[position])
    }

    override fun getItemCount() = syllabusList.size
}