package com.roni.class12thagjetnotes.jet.models

import com.google.firebase.firestore.PropertyName


// Base Material class
abstract class JetMaterial {
    @PropertyName("id")
    var id: String = ""

    @PropertyName("title")
    var title: String = ""

    @PropertyName("description")
    var description: String = ""

    @PropertyName("thumbnailUrl")
    var thumbnailUrl: String = ""

    @PropertyName("timestamp")
    var timestamp: Long = 0

    @PropertyName("downloadCount")
    var downloadCount: Int = 0

    @PropertyName("viewCount")
    var viewCount: Int = 0

    @PropertyName("isNew")
    var isNew: Boolean = false

    @PropertyName("isPremium")
    var isPremium: Boolean = false
}

// Syllabus Topic Model
data class JetSyllabusTopic(
    @PropertyName("subject")
    var subject: String = "",

    @PropertyName("unit")
    var unit: String = "",

    @PropertyName("unitNumber")
    var unitNumber: Int = 0,

    @PropertyName("topics")
    var topics: List<String> = listOf(),

    @PropertyName("importance")
    var importance: String = "", // "High", "Medium", "Low"

    @PropertyName("weightage")
    var weightage: String = "",

    @PropertyName("referenceLinks")
    var referenceLinks: List<String> = listOf()
) : JetMaterial() {
    constructor() : this("", "", 0, listOf(), "", "", listOf())
}

// User Progress Model
data class JetUserProgress(
    @PropertyName("userId")
    var userId: String = "",

    @PropertyName("materialId")
    var materialId: String = "",

    @PropertyName("materialType")
    var materialType: String = "", // "pdf", "video", "quiz", etc.

    @PropertyName("status")
    var status: String = "", // "not_started", "in_progress", "completed"

    @PropertyName("progress")
    var progress: Int = 0, // 0-100

    @PropertyName("lastAccessedTime")
    var lastAccessedTime: Long = 0,

    @PropertyName("completedTime")
    var completedTime: Long = 0,

    @PropertyName("bookmarked")
    var bookmarked: Boolean = false,

    @PropertyName("notes")
    var notes: String = "",

    @PropertyName("rating")
    var rating: Float = 0f
) {
    constructor() : this("", "", "", "", 0, 0, 0, false, "", 0f)
}


// Model for JetPdfNotesActivity
data class JetPdfNote(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val pdfUrl: String = "",
    val subject: String = "",
    val topic: String = "",
    val isPremium: Boolean = false,
    val viewCount: Int = 0,
    val timestamp: Long = 0
)

// Model for JetVideosActivity
data class JetVideo(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val youtubeId: String = "",
    val subject: String = "",
    val topic: String = "",
    val duration: String = "",
    val isPremium: Boolean = false,
    val viewCount: Int = 0,
    val timestamp: Long = 0
)

// Model for JetPyqsActivity
data class JetPyq(
    var id: String = "",
    val title: String = "",
    val year: Int = 2024,
    val pdfUrl: String = "",
    val solutionsUrl: String = "",
    val hasSolutions: Boolean = false,
    val subject: String = "",
    val isPremium: Boolean = false,
    val downloadCount: Int = 0,
    val timestamp: Long = 0
)

// Model for JetQuizzesActivity
data class JetQuiz(
    var id: String = "",
    val quizId: String = "", // ID to fetch questions from a different collection
    val title: String = "",
    val description: String = "",
    val subject: String = "",
    val topic: String = "",
    val totalQuestions: Int = 0,
    val duration: Int = 0, // in minutes
    val difficulty: String = "Medium", // Easy, Medium, Hard
    val totalMarks: Int = 0,
    val isPremium: Boolean = false,
    val timestamp: Long = 0
)

// Model for JetSyllabusActivity
data class JetSyllabus(
    var id: String = "",
    val topicName: String = "",
    val description: String = "",
    val subject: String = "",
    val topicOrder: Int = 0, // For ordering
    val isPremium: Boolean = false
)

// Model for JetMindMapsActivity
data class JetMindMap(
    var id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val subject: String = "",
    val topic: String = "",
    val isPremium: Boolean = false,
    val timestamp: Long = 0
)

// Model for JetTipsActivity
data class JetTip(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val subject: String = "",
    val category: String = "", // e.g., "Exam Strategy", "Time Management"
    val isPremium: Boolean = false,
    val timestamp: Long = 0
)

// Model for JetTaiyariActivity Updates
data class JetUpdate(
    val title: String = "",
    val description: String = "",
    val link: String = "",
    val imageUrl: String = "",
    val timestamp: Long = 0
)