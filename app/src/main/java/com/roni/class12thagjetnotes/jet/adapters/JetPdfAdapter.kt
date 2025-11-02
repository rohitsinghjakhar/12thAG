package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.jet.models.JetPdfNote
import kotlinx.android.synthetic.main.item_jet_pdf.view.*

class JetPdfAdapter(
    private val pdfList: List<JetPdfNote>,
    private val onClick: (JetPdfNote) -> Unit
) : RecyclerView.Adapter<JetPdfAdapter.PdfViewHolder>() {

    inner class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pdf: JetPdfNote) {
            itemView.tvPdfTitle.text = pdf.title
            itemView.tvPdfDescription.text = pdf.description
            itemView.tvPdfViews.text = "${pdf.viewCount} views"

            if (pdf.isPremium) {
                itemView.ivPremiumIcon.visibility = View.VISIBLE
            } else {
                itemView.ivPremiumIcon.visibility = View.GONE
            }

            itemView.setOnClickListener { onClick(pdf) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jet_pdf, parent, false)
        return PdfViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.bind(pdfList[position])
    }

    override fun getItemCount(): Int = pdfList.size
}