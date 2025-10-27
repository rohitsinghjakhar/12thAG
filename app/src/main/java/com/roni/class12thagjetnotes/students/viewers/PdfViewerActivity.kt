package com.roni.class12thagjetnotes.students.viewers

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.roni.class12thagjetnotes.databinding.ActivityPdfViewerBinding
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.*
import java.net.URL

class PdfViewerActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener, OnErrorListener {

    private lateinit var binding: ActivityPdfViewerBinding
    private var pdfUrl: String = ""
    private var pdfTitle: String = ""
    private var pageNumber: Int = 0
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pdfUrl = intent.getStringExtra("pdf_url") ?: ""
        pdfTitle = intent.getStringExtra("title") ?: "PDF Document"

        setupToolbar()

        if (pdfUrl.isNotEmpty()) {
            loadPdf()
        } else {
            Toast.makeText(this, "Invalid PDF URL", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = pdfTitle
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadPdf() {
        binding.progressBar.visibility = View.VISIBLE

        if (pdfUrl.startsWith("http")) {
            // Load from URL
            loadPdfFromUrl()
        } else {
            // Load from local file
            loadPdfFromFile()
        }
    }

    private fun loadPdfFromUrl() {
        scope.launch {
            try {
                val file = withContext(Dispatchers.IO) {
                    downloadPdf(pdfUrl)
                }

                if (file != null) {
                    displayPdf(file)
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@PdfViewerActivity, "Failed to download PDF", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@PdfViewerActivity, "Error loading PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun downloadPdf(urlString: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(urlString)
                val connection = url.openConnection()
                connection.connect()

                val fileName = "temp_${System.currentTimeMillis()}.pdf"
                val file = File(cacheDir, fileName)

                connection.getInputStream().use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                file
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun loadPdfFromFile() {
        try {
            val file = File(pdfUrl)
            if (file.exists()) {
                displayPdf(file)
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "PDF file not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Error loading PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayPdf(file: File) {
        binding.pdfView.fromFile(file)
            .defaultPage(pageNumber)
            .onPageChange(this)
            .enableAnnotationRendering(true)
            .onLoad(this)
            .scrollHandle(DefaultScrollHandle(this))
            .spacing(10)
            .onError(this)
            .load()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        binding.toolbar.subtitle = "Page ${page + 1} of $pageCount"
    }

    override fun loadComplete(nbPages: Int) {
        binding.progressBar.visibility = View.GONE
        binding.pdfView.visibility = View.VISIBLE
    }

    override fun onError(t: Throwable?) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, "Error displaying PDF: ${t?.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}