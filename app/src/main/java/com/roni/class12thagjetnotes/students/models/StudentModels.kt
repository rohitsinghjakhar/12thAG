package com.roni.class12thagjetnotes.students.models

data class ClassModel(
    val id: String = "",
    val name: String = "",
    val grade: String = "",
    val description: String = "",
    val thumbnail: String = "",
    val order: Int = 0
)

data class Subject(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val icon: String = "",
    val classId: String = "",
    val order: Int = 0
)

data class Medium(
    val id: String = "",
    val name: String = "", // "Hindi" or "English"
    val subjectId: String = ""
)

data class Chapter(
    val id: String = "",
    val name: String = "",
    val chapterNumber: Int = 0,
    val description: String = "",
    val mediumId: String = "",
    val order: Int = 0
)

data class Content(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "", // "notes", "books", "quizzes", "videos", "mindmaps"
    val url: String = "",
    val thumbnailUrl: String = "",
    val chapterId: String = "",
    val order: Int = 0,
    val duration: String = "", // For videos
    val fileSize: String = "", // For PDFs
    val uploadedAt: Long = 0
)

// Quiz Models
data class Quiz(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val chapterId: String = "",
    val timeLimit: Int = 0, // in minutes, 0 = no limit
    val passingScore: Int = 50, // percentage
    val totalQuestions: Int = 0,
    val order: Int = 0,
    val createdAt: Long = 0
)

data class Question(
    val id: String = "",
    val quizId: String = "",
    val questionText: String = "",
    val option1: String = "",
    val option2: String = "",
    val option3: String = "",
    val option4: String = "",
    val correctAnswer: Int = 1, // 1, 2, 3, or 4
    val explanation: String = "",
    val order: Int = 0
)

data class QuizResult(
    val id: String = "",
    val quizId: String = "",
    val userId: String = "",
    val userName: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val timeTaken: Long = 0, // in seconds
    val answers: Map<String, Int> = emptyMap(), // questionId to selected answer
    val completedAt: Long = 0,
    val passed: Boolean = false
)