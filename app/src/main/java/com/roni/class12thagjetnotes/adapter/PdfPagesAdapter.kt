package com.roni.class12thagjetnotes.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roni.class12thagjetnotes.databinding.ItemPdfPageBinding

class PdfPagesAdapter(
    private val pages: List<Bitmap>
) : RecyclerView.Adapter<PdfPagesAdapter.PageViewHolder>() {

    private var zoomLevel = 1.0f

    inner class PageViewHolder(private val binding: ItemPdfPageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap) {
            binding.ivPdfPage.setImageBitmap(bitmap)
            binding.ivPdfPage.scaleX = zoomLevel
            binding.ivPdfPage.scaleY = zoomLevel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemPdfPageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount() = pages.size

    fun zoomIn() {
        if (zoomLevel < 3.0f) {
            zoomLevel += 0.25f
            notifyDataSetChanged()
        }
    }

    fun zoomOut() {
        if (zoomLevel > 0.5f) {
            zoomLevel -= 0.25f
            notifyDataSetChanged()
        }
    }
}