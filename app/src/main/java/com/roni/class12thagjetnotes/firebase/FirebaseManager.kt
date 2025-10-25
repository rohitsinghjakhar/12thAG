package com.roni.class12thagjetnotes.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.roni.class12thagjetnotes.models.firebase.*
import com.roni.class12thagjetnotes.models.quiz.*
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

    // ========== QUIZ ==========
    suspend fun getQuizzesBySubject(subject: String? = null): Result<List<Quiz>> {
        return try {
            var query = firestore.collection("quizzes") as Query

            if (subject != null) {
                query = query.whereEqualTo("subject", subject)
            }

            val snapshot = query.get().await()
            val quizzes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Quiz::class.java)
            }
            Result.success(quizzes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChaptersBySubject(subjectId: String): Result<List<Chapter>> {
        return try {
            val snapshot = firestore.collection("chapters")
                .whereEqualTo("subjectId", subjectId)
                .orderBy("chapterNumber", Query.Direction.ASCENDING)
                .get()
                .await()

            val chapters = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Chapter::class.java)
            }
            Result.success(chapters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuizzesByChapter(chapterId: String): Result<List<Quiz>> {
        return try {
            val snapshot = firestore.collection("quizzes")
                .whereEqualTo("chapterId", chapterId)
                .get()
                .await()

            val quizzes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Quiz::class.java)
            }
            Result.success(quizzes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuizById(quizId: String): Result<QuizDetail> {
        return try {
            val snapshot = firestore.collection("quiz_details")
                .document(quizId)
                .get()
                .await()

            val quizDetail = snapshot.toObject(QuizDetail::class.java)
            if (quizDetail != null) {
                Result.success(quizDetail)
            } else {
                Result.failure(Exception("Quiz not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveQuizResult(result: QuizResult): Result<String> {
        return try {
            val docRef = firestore.collection("quiz_results").document()
            val resultWithId = result.copy(id = docRef.id)
            docRef.set(resultWithId).await()

            // Increment quiz attempts
            firestore.collection("quizzes").document(result.quizId)
                .update("attempts", com.google.firebase.firestore.FieldValue.increment(1))
                .await()

            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserQuizResults(userId: String): Result<List<QuizResult>> {
        return try {
            val snapshot = firestore.collection("quiz_results")
                .whereEqualTo("userId", userId)
                .orderBy("completedAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val results = snapshot.documents.mapNotNull { doc ->
                doc.toObject(QuizResult::class.java)
            }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}