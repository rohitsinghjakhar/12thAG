package com.roni.class12thagjetnotes.jet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_jet_pdf.view.*

class JetPdfAdapter(
    private val pdfList: List<JetPdfNote>,
    private val onPdfClick: (JetPdfNote) -> Unit
) : RecyclerView.Adapter<JetPdfAdapter.PdfViewHolder>() {

    inner class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardPdf: CardView = itemView.cardPdf
        val ivThumbnail: ImageView = itemView.ivPdfThumbnail
        val tvTitle: TextView = itemView.tvPdfTitle
        val tvSubject: TextView = itemView.tvPdfSubject
        val tvTopic: TextView = itemView.tvPdfTopic
        val tvPages: TextView = itemView.tvPdfPages
        val tvSize: TextView = itemView.tvPdfSize
        val tvDownloads: TextView = itemView.tvPdfDownloads
        val tvNewBadge: TextView = itemView.tvNewBadge
        val tvPremiumBadge: TextView = itemView.tvPremiumBadge
        val btnDownload: ImageView = itemView.btnDownloadPdf

        fun bind(pdf: JetPdfNote) {
            tvTitle.text = pdf.title
            tvSubject.text = pdf.subject
            tvTopic.text = pdf.topic
            tvPages.text = "${pdf.pages} Pages"
            tvSize.text = pdf.fileSize
            tvDownloads.text = "${pdf.downloadCount} downloads"

            // Show/hide badges
            tvNewBadge.visibility = if (pdf.isNew) View.VISIBLE else View.GONE
            tvPremiumBadge.visibility = if (pdf.isPremium) View.VISIBLE else View.GONE

            // Load thumbnail
            if (pdf.thumbnailUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(pdf.thumbnailUrl)
                    .centerCrop()
                    .placeholder(R.drawable.pdf_placeholder)
                    .into(ivThumbnail)
            } else {
                ivThumbnail.setImageResource(R.drawable.pdf_placeholder)
            }

            // Click listeners
            cardPdf.setOnClickListener {
                onPdfClick(pdf)
            }

            btnDownload.setOnClickListener {
                // Handle download separately if needed
                onPdfClick(pdf)
            }
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