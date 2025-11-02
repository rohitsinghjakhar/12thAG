package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.jet.models.JetSyllabus
import kotlinx.android.synthetic.main.item_jet_syllabus.view.* // You need to create item_jet_syllabus.xml

class JetSyllabusAdapter(
    private val syllabusList: List<JetSyllabus>,
    private val onClick: (JetSyllabus) -> Unit
) : RecyclerView.Adapter<JetSyllabusAdapter.SyllabusViewHolder>() {

    inner class SyllabusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(topic: JetSyllabus, position: Int) {
            itemView.tvTopicNumber.text = (position + 1).toString()
            itemView.tvTopicTitle.text = topic.topicName
            itemView.tvTopicDescription.text = topic.description

            if (topic.isPremium) {
                itemView.ivPremiumIcon.visibility = View.VISIBLE
            } else {
                itemView.ivPremiumIcon.visibility = View.GONE
            }

            itemView.setOnClickListener { onClick(topic) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyllabusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_syllabus, parent, false)
        return SyllabusViewHolder(view)
    }

    override fun onBindViewHolder(holder: SyllabusViewHolder, position: Int) {
        holder.bind(syllabusList[position], position)
    }

    override fun getItemCount(): Int = syllabusList.size
}