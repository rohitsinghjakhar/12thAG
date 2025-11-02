package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.jet.models.JetPyq
import kotlinx.android.synthetic.main.item_jet_pyq.view.*

class JetPyqAdapter(
    private val pyqList: List<JetPyq>,
    private val onClick: (JetPyq) -> Unit
) : RecyclerView.Adapter<JetPyqAdapter.PyqViewHolder>() {

    inner class PyqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pyq: JetPyq) {
            itemView.tvPyqTitle.text = pyq.title
            itemView.tvPyqYear.text = pyq.year.toString()

            if (pyq.hasSolutions) {
                itemView.chipSolutions.visibility = View.VISIBLE
            } else {
                itemView.chipSolutions.visibility = View.GONE
            }

            if (pyq.isPremium) {
                itemView.ivPremiumIcon.visibility = View.VISIBLE
            } else {
                itemView.ivPremiumIcon.visibility = View.GONE
            }

            itemView.setOnClickListener { onClick(pyq) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PyqViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_pyq, parent, false)
        return PyqViewHolder(view)
    }

    override fun onBindViewHolder(holder: PyqViewHolder, position: Int) {
        holder.bind(pyqList[position])
    }

    override fun getItemCount(): Int = pyqList.size
}