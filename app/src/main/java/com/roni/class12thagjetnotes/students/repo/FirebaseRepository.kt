package com.roni.class12thagjetnotes.students.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.roni.class12thagjetnotes.students.models.*
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        private const val TAG = "FirebaseRepository"
        private const val COLLECTION_CLASSES = "classes"
        private const val COLLECTION_SUBJECTS = "subjects"
        private const val COLLECTION_MEDIUMS = "mediums"
        private const val COLLECTION_CHAPTERS = "chapters"
        private const val COLLECTION_CONTENT = "content"
        private const val COLLECTION_QUIZZES = "quizzes"
        private const val COLLECTION_QUESTIONS = "questions"
        private const val COLLECTION_QUIZ_RESULTS = "quiz_results"
    }

    suspend fun getClasses(): List<ClassModel> {
        return try {
            val snapshot = firestore.collection(COLLECTION_CLASSES)
                .orderBy("order", Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ClassModel::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading classes", e)
            emptyList()
        }
    }

    suspend fun getSubjects(classId: String): List<Subject> {
        return try {
            val snapshot = firestore.collection(COLLECTION_SUBJECTS)
                .whereEqualTo("classId", classId)
                .orderBy("order", Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Subject::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading subjects", e)
            emptyList()
        }
    }

    suspend fun getMediums(subjectId: String): List<Medium> {
        return try {
            val snapshot = firestore.collection(COLLECTION_MEDIUMS)
                .whereEqualTo("subjectId", subjectId)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Medium::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading mediums", e)
            emptyList()
        }
    }

    suspend fun getChapters(mediumId: String): List<Chapter> {
        return try {
            val snapshot = firestore.collection(COLLECTION_CHAPTERS)
                .whereEqualTo("mediumId", mediumId)
                .orderBy("order", Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Chapter::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading chapters", e)
            emptyList()
        }
    }

    suspend fun getContent(chapterId: String, type: String): List<Content> {
        return try {
            val snapshot = firestore.collection(COLLECTION_CONTENT)
                .whereEqualTo("chapterId", chapterId)
                .whereEqualTo("type", type)
                .orderBy("order", Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Content::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading content", e)
            emptyList()
        }
    }

    // Quiz Methods
    suspend fun getQuizzes(chapterId: String): List<Quiz> {
        return try {
            val snapshot = firestore.collection(COLLECTION_QUIZZES)
                .whereEqualTo("chapterId", chapterId)
                .orderBy("order", Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Quiz::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading quizzes", e)
            emptyList()
        }
    }

    suspend fun getQuiz(quizId: String): Quiz? {
        return try {
            val snapshot = firestore.collection(COLLECTION_QUIZZES)
                .document(quizId)
                .get()
                .await()

            snapshot.toObject(Quiz::class.java)?.copy(id = snapshot.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading quiz", e)
            null
        }
    }

    suspend fun getQuestions(quizId: String): List<Question> {
        return try {
            val snapshot = firestore.collection(COLLECTION_QUESTIONS)
                .whereEqualTo("quizId", quizId)
                .orderBy("order", Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Question::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading questions", e)
            emptyList()
        }
    }

    suspend fun saveQuizResult(result: QuizResult): Boolean {
        return try {
            firestore.collection(COLLECTION_QUIZ_RESULTS)
                .add(result)
                .await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error saving quiz result", e)
            false
        }
    }

    suspend fun getUserQuizResults(userId: String, quizId: String): List<QuizResult> {
        return try {
            val snapshot = firestore.collection(COLLECTION_QUIZ_RESULTS)
                .whereEqualTo("userId", userId)
                .whereEqualTo("quizId", quizId)
                .orderBy("completedAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(QuizResult::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading quiz results", e)
            emptyList()
        }
    }
}