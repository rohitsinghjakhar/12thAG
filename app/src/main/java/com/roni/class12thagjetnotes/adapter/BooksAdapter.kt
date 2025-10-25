package com.roni.class12thagjetnotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.roni.class12thagjetnotes.R
import com.roni.class12thagjetnotes.databinding.ItemBookBinding
import com.roni.class12thagjetnotes.models.firebase.Book

class BooksAdapter(
    private val books: List<Book>,
    private val onItemClick: (Book) -> Unit,
    private val onDownloadClick: (Book) -> Unit
) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.apply {
                tvTitle.text = book.title
                tvAuthor.text = book.author
                tvRating.text = "${book.rating}"
                tvPages.text = "${book.pages} pages"

                // Load cover image
                Glide.with(root.context)
                    .load(book.coverImageUrl)
                    .placeholder(R.drawable.placeholder_book)
                    .into(ivCover)

                root.setOnClickListener { onItemClick(book) }
                btnDownload.setOnClickListener { onDownloadClick(book) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size
}