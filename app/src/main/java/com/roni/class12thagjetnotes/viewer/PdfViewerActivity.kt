package com.roni.class12thagjetnotes.viewer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.roni.class12thagjetnotes.adapter.PdfPagesAdapter
import com.roni.class12thagjetnotes.databinding.ActivityPdfViewerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class PdfViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewerBinding
    private var pdfRenderer: PdfRenderer? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null
    private var currentPage = 0
    private val pageImages = mutableListOf<Bitmap>()
    private lateinit var adapter: PdfPagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pdfUrl = intent.getStringExtra("pdf_url") ?: ""
        val pdfTitle = intent.getStringExtra("pdf_title") ?: "PDF Document"

        setupUI(pdfTitle)
        loadPdf(pdfUrl)
    }

    private fun setupUI(title: String) {
        binding.tvPdfTitle.text = title

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnZoomIn.setOnClickListener {
            adapter.zoomIn()
        }

        binding.btnZoomOut.setOnClickListener {
            adapter.zoomOut()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = PdfPagesAdapter(pageImages)
        binding.rvPdfPages.layoutManager = LinearLayoutManager(this)
        binding.rvPdfPages.adapter = adapter

        // Add snap helper for smooth page scrolling
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvPdfPages)

        // Track current page
        binding.rvPdfPages.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    currentPage = layoutManager.findFirstVisibleItemPosition()
                    updatePageIndicator()
                }
            }
        })
    }

    private fun loadPdf(pdfUrl: String) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val file = withContext(Dispatchers.IO) {
                    downloadPdf(pdfUrl)
                }

                if (file != null) {
                    openPdfFile(file)
                    showLoading(false)
                } else {
                    showLoading(false)
                    showError("Failed to download PDF")
                }
            } catch (e: Exception) {
                showLoading(false)
                showError("Error loading PDF: ${e.message}")
            }
        }
    }

    private suspend fun downloadPdf(pdfUrl: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(pdfUrl)
                val connection = url.openConnection()
                connection.connect()

                val file = File(cacheDir, "temp_pdf.pdf")
                val outputStream = FileOutputStream(file)
                val inputStream = connection.getInputStream()

                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.close()
                inputStream.close()
                file
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun openPdfFile(file: File) {
        try {
            parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(parcelFileDescriptor!!)

            val pageCount = pdfRenderer?.pageCount ?: 0
            binding.tvTotalPages.text = "of $pageCount"

            // Render all pages
            lifecycleScope.launch(Dispatchers.Default) {
                for (i in 0 until pageCount) {
                    val bitmap = renderPage(i)
                    withContext(Dispatchers.Main) {
                        pageImages.add(bitmap)
                        adapter.notifyItemInserted(pageImages.size - 1)
                    }
                }
                withContext(Dispatchers.Main) {
                    updatePageIndicator()
                }
            }
        } catch (e: Exception) {
            showError("Error opening PDF: ${e.message}")
        }
    }

    private fun renderPage(pageIndex: Int): Bitmap {
        val page = pdfRenderer?.openPage(pageIndex)

        // Create bitmap with appropriate size
        val bitmap = Bitmap.createBitmap(
            page?.width ?: 1080,
            page?.height ?: 1920,
            Bitmap.Config.ARGB_8888
        )

        page?.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page?.close()

        return bitmap
    }

    private fun updatePageIndicator() {
        binding.tvCurrentPage.text = "${currentPage + 1}"
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvPdfPages.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        pdfRenderer?.close()
        parcelFileDescriptor?.close()

        // Clean up bitmaps
        pageImages.forEach { it.recycle() }
        pageImages.clear()
    }
}