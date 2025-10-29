package com.roni.class12thagjetnotes.study

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.roni.class12thagjetnotes.adapter.BooksAdapter
import com.roni.class12thagjetnotes.databinding.ActivityBooksBinding
import com.roni.class12thagjetnotes.firebase.FirebaseManager
import com.roni.class12thagjetnotes.models.firebase.Book
import com.roni.class12thagjetnotes.students.viewers.PdfViewerActivity
import kotlinx.coroutines.launch

class BooksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBooksBinding
    private val allBooks = mutableListOf<Book>()
    private val filteredBooks = mutableListOf<Book>()
    private lateinit var adapter: BooksAdapter
    private var selectedSubject: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedSubject = intent.getStringExtra("subject_name") ?: "All"

        setupUI()
        setupRecyclerView()
        loadBooks()
    }

    private fun setupUI() {
        // Fixed: Using tvTitle with proper null safety
        binding.tvTitle?.text = if (selectedSubject != "All") {
            "$selectedSubject Books"
        } else {
            "All Books"
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterBooks(newText ?: "")
                return true
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            loadBooks()
        }
    }

    private fun setupRecyclerView() {
        adapter = BooksAdapter(
            books = filteredBooks,
            onItemClick = { book ->
                openBook(book)
            },
            onDownloadClick = { book ->
                downloadBook(book)
            }
        )

        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = adapter
    }

    private fun loadBooks() {
        showLoading(true)

        lifecycleScope.launch {
            val result = FirebaseManager.getBooks(
                if (selectedSubject != "All") selectedSubject else null
            )

            result.onSuccess { books ->
                allBooks.clear()
                allBooks.addAll(books)
                filteredBooks.clear()
                filteredBooks.addAll(books)
                adapter.notifyDataSetChanged()
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                updateUI()
            }.onFailure { error ->
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                showEmptyState()
                showError(error.message ?: "Failed to load books")
            }
        }
    }

    private fun filterBooks(query: String) {
        val filtered = if (query.isEmpty()) {
            allBooks
        } else {
            allBooks.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.author.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }

        filteredBooks.clear()
        filteredBooks.addAll(filtered)
        adapter.notifyDataSetChanged()
        updateUI()
    }

    private fun openBook(book: Book) {
        if (book.pdfUrl.isEmpty()) {
            showError("Book PDF not available")
            return
        }

        val intent = Intent(this, PdfViewerActivity::class.java)
        intent.putExtra("pdf_url", book.pdfUrl)
        intent.putExtra("pdf_title", book.title)
        startActivity(intent)
    }

    private fun downloadBook(book: Book) {
        lifecycleScope.launch {
            FirebaseManager.incrementBookDownloads(book.id)
        }
        openBook(book)
        showError("Opening Book...")
    }

    private fun updateUI() {
        binding.tvTotalBooks?.text = "${filteredBooks.size} Books"

        if (filteredBooks.isEmpty()) {
            showEmptyState()
        } else {
            hideEmptyState()
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.rvBooks.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.rvBooks.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}