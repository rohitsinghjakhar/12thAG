package com.roni.class12thagjetnotes.utils

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.roni.class12thagjetnotes.models.firebase.Book
import com.roni.class12thagjetnotes.models.firebase.PdfNote
import com.roni.class12thagjetnotes.models.firebase.PreviousPaper
import com.roni.class12thagjetnotes.models.firebase.Syllabus
import com.roni.class12thagjetnotes.models.firebase.Video
import kotlinx.coroutines.tasks.await

/**
 * Utility class to upload sample data to Firebase Firestore
 */
object FirebaseDataUploader {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun uploadSampleVideos() {
        val videos = listOf(
            Video(
                id = "v1",
                title = "Relations and Functions - Complete Chapter",
                subject = "Mathematics",
                chapter = "Chapter 1",
                description = "Complete explanation of Relations and Functions with solved examples",
                thumbnailUrl = "https://example.com/thumbnails/math_relations.jpg",
                videoUrl = "https://www.youtube.com/watch?v=example1",
                duration = "45:30",
                views = 1234,
                instructor = "Prof. Sharma",
                uploadedAt = Timestamp.now(),
                tags = listOf("mathematics", "relations", "functions", "class12")
            ),
            Video(
                id = "v2",
                title = "Electric Charges and Fields",
                subject = "Physics",
                chapter = "Chapter 1",
                description = "Understanding electric charges and electric fields with practical examples",
                thumbnailUrl = "https://example.com/thumbnails/physics_electric.jpg",
                videoUrl = "https://www.youtube.com/watch?v=example2",
                duration = "52:15",
                views = 2100,
                instructor = "Dr. Singh",
                uploadedAt = Timestamp.now(),
                tags = listOf("physics", "electricity", "fields", "class12")
            )
        )

        videos.forEach { video ->
            firestore.collection("videos").document(video.id).set(video).await()
        }
    }

    suspend fun uploadSamplePdfNotes() {
        val notes = listOf(
            PdfNote(
                id = "pdf1",
                title = "Matrices and Determinants - Complete Notes",
                subject = "Mathematics",
                chapter = "Chapter 3",
                description = "Comprehensive notes covering all topics of Matrices and Determinants",
                pdfUrl = "pdfs/mathematics/matrices_notes.pdf",
                thumbnailUrl = "https://example.com/thumbnails/matrices.jpg",
                pages = 45,
                size = "2.3 MB",
                downloads = 567,
                uploadedAt = Timestamp.now(),
                category = "Notes",
                rating = 4.5f,  // Changed to Float
                tags = listOf("mathematics", "matrices", "determinants")
            ),
            PdfNote(
                id = "pdf2",
                title = "Organic Chemistry Revision Notes",
                subject = "Chemistry",
                chapter = "Chapters 10-16",
                description = "Quick revision notes for organic chemistry with reaction mechanisms",
                pdfUrl = "pdfs/chemistry/organic_revision.pdf",
                thumbnailUrl = "https://example.com/thumbnails/organic_chem.jpg",
                pages = 60,
                size = "3.1 MB",
                downloads = 890,
                uploadedAt = Timestamp.now(),
                category = "Revision",
                rating = 4.7f,  // Changed to Float
                tags = listOf("chemistry", "organic", "reactions")
            )
        )

        notes.forEach { note ->
            firestore.collection("pdf_notes").document(note.id).set(note).await()
        }
    }

    suspend fun uploadSampleBooks() {
        val books = listOf(
            Book(
                id = "book1",
                title = "NCERT Mathematics Class 12",
                author = "NCERT",
                subject = "Mathematics",
                description = "Official NCERT textbook for Class 12 Mathematics",
                coverImageUrl = "https://example.com/covers/ncert_math_12.jpg",
                pdfUrl = "books/ncert_mathematics_12.pdf",
                pages = 410,
                edition = "2024",
                publisher = "NCERT",
                rating = 4.8f,  // Changed to Float
                downloads = 5670,
                uploadedAt = Timestamp.now(),
                isbn = "978-8174507181",
                tags = listOf("ncert", "mathematics", "textbook")
            ),
            Book(
                id = "book2",
                title = "Concepts of Physics - H.C. Verma",
                author = "H.C. Verma",
                subject = "Physics",
                description = "Comprehensive physics book with detailed explanations and problems",
                coverImageUrl = "https://example.com/covers/hc_verma.jpg",
                pdfUrl = "books/hc_verma_physics.pdf",
                pages = 462,
                edition = "2023",
                publisher = "Bharati Bhawan",
                rating = 4.9f,  // Changed to Float
                downloads = 8900,
                uploadedAt = Timestamp.now(),
                isbn = "978-8177091878",
                tags = listOf("physics", "hc verma", "reference")
            )
        )

        books.forEach { book ->
            firestore.collection("books").document(book.id).set(book).await()
        }
    }

    suspend fun uploadSamplePreviousPapers() {
        val papers = listOf(
            PreviousPaper(
                id = "paper1",
                title = "JET Mathematics 2024",
                year = "2024",  // Changed to String
                exam = "JET",
                subject = "Mathematics",
                pdfUrl = "papers/jet_math_2024.pdf",
                solutionUrl = "papers/jet_math_2024_solution.pdf",
                downloads = 1234,
                uploadedAt = Timestamp.now(),
                totalQuestions = 100,
                totalMarks = 400,
                duration = 180
            ),
            PreviousPaper(
                id = "paper2",
                title = "CBSE Physics Board Paper 2024",
                year = "2024",  // Changed to String
                exam = "CBSE Board",
                subject = "Physics",
                pdfUrl = "papers/cbse_physics_2024.pdf",
                solutionUrl = "papers/cbse_physics_2024_solution.pdf",
                downloads = 2100,
                uploadedAt = Timestamp.now(),
                totalQuestions = 33,
                totalMarks = 70,
                duration = 180
            )
        )

        papers.forEach { paper ->
            firestore.collection("previous_papers").document(paper.id).set(paper).await()
        }
    }

    suspend fun uploadSampleSyllabus() {
        val syllabusItems = listOf(
            Syllabus(
                id = "syl1",
                title = "CBSE Class 12 Mathematics Syllabus",
                subject = "Mathematics",
                description = "Complete syllabus for CBSE Class 12 Mathematics",
                pdfUrl = "syllabus/cbse_math_12.pdf",
                topics = listOf(
                    "Relations and Functions",
                    "Inverse Trigonometric Functions",
                    "Matrices and Determinants",
                    "Continuity and Differentiability",
                    "Applications of Derivatives",
                    "Integrals",
                    "Applications of Integrals",
                    "Differential Equations",
                    "Vector Algebra",
                    "Three Dimensional Geometry",
                    "Linear Programming",
                    "Probability"
                ),
                totalMarks = 100,
                duration = "3 hours",
                examBoard = "CBSE",
                year = 2024,
                uploadedAt = Timestamp.now()
            ),
            Syllabus(
                id = "syl2",
                title = "CBSE Class 12 Physics Syllabus",
                subject = "Physics",
                description = "Complete syllabus for CBSE Class 12 Physics",
                pdfUrl = "syllabus/cbse_physics_12.pdf",
                topics = listOf(
                    "Electric Charges and Fields",
                    "Electrostatic Potential and Capacitance",
                    "Current Electricity",
                    "Moving Charges and Magnetism",
                    "Magnetism and Matter",
                    "Electromagnetic Induction",
                    "Alternating Current",
                    "Electromagnetic Waves",
                    "Ray Optics and Optical Instruments",
                    "Wave Optics",
                    "Dual Nature of Radiation and Matter",
                    "Atoms and Nuclei",
                    "Semiconductor Electronics"
                ),
                totalMarks = 70,
                duration = "3 hours",
                examBoard = "CBSE",
                year = 2024,
                uploadedAt = Timestamp.now()
            )
        )

        syllabusItems.forEach { item ->
            firestore.collection("syllabus").document(item.id).set(item).await()
        }
    }

    /**
     * Call this function to upload all sample data at once
     */
    suspend fun uploadAllSampleData() {
        try {
            uploadSampleVideos()
            uploadSamplePdfNotes()
            uploadSampleBooks()
            uploadSamplePreviousPapers()
            uploadSampleSyllabus()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}