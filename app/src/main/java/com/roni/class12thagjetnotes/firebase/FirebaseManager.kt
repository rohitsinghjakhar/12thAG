package com.roni.class12thagjetnotes.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.roni.class12thagjetnotes.models.firebase.*
import kotlinx.coroutines.tasks.await

object FirebaseManager {

    private val firestore = FirebaseFirestore.getInstance()

    // ========== SUBJECTS ==========
    suspend fun getSubjects(): Result<List<Subject>> {
        return try {
            val snapshot = firestore.collection("subjects")
                .orderBy("order", Query.Direction.ASCENDING)
                .get()
                .await()

            val subjects = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Subject::class.java)
            }
            Result.success(subjects)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== VIDEOS ==========
    suspend fun getVideos(subject: String? = null): Result<List<Video>> {
        return try {
            var query = firestore.collection("videos") as Query

            if (subject != null) {
                query = query.whereEqualTo("subject", subject)
            }

            val snapshot = query.get().await()
            val videos = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Video::class.java)
            }
            Result.success(videos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementVideoViews(videoId: String) {
        try {
            firestore.collection("videos").document(videoId)
                .update("views", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ========== PDF NOTES ==========
    suspend fun getPdfNotes(subject: String? = null): Result<List<PdfNote>> {
        return try {
            var query = firestore.collection("pdf_notes") as Query

            if (subject != null) {
                query = query.whereEqualTo("subject", subject)
            }

            val snapshot = query.get().await()
            val notes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(PdfNote::class.java)
            }
            Result.success(notes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementPdfDownloads(noteId: String) {
        try {
            firestore.collection("pdf_notes").document(noteId)
                .update("downloads", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ========== BOOKS ==========
    suspend fun getBooks(subject: String? = null): Result<List<Book>> {
        return try {
            var query = firestore.collection("books") as Query

            if (subject != null) {
                query = query.whereEqualTo("subject", subject)
            }

            val snapshot = query.get().await()
            val books = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Book::class.java)
            }
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementBookDownloads(bookId: String) {
        try {
            firestore.collection("books").document(bookId)
                .update("downloads", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ========== PREVIOUS PAPERS ==========
    suspend fun getPreviousPapers(subject: String? = null): Result<List<PreviousPaper>> {
        return try {
            var query = firestore.collection("previous_papers") as Query

            if (subject != null) {
                query = query.whereEqualTo("subject", subject)
            }

            val snapshot = query.orderBy("year", Query.Direction.DESCENDING).get().await()
            val papers = snapshot.documents.mapNotNull { doc ->
                doc.toObject(PreviousPaper::class.java)
            }
            Result.success(papers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementPaperDownloads(paperId: String) {
        try {
            firestore.collection("previous_papers").document(paperId)
                .update("downloads", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ========== SYLLABUS ==========
    suspend fun getSyllabus(subject: String? = null): Result<List<Syllabus>> {
        return try {
            var query = firestore.collection("syllabus") as Query

            if (subject != null) {
                query = query.whereEqualTo("subject", subject)
            }

            val snapshot = query.get().await()
            val syllabusItems = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Syllabus::class.java)
            }
            Result.success(syllabusItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}