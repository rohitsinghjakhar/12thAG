package com.roni.class12thagjetnotes.jet.models

import com.google.firebase.firestore.PropertyName

data class JetUpdate(
    @PropertyName("id")
    var id: String = "",

    @PropertyName("title")
    var title: String = "",

    @PropertyName("description")
    var description: String = "",

    @PropertyName("date")
    var date: String = "",

    @PropertyName("type")
    var type: String = "", // "news", "announcement", "exam_date", "result", etc.

    @PropertyName("link")
    var link: String = "",

    @PropertyName("imageUrl")
    var imageUrl: String = "",

    @PropertyName("timestamp")
    var timestamp: Long = 0,

    @PropertyName("isImportant")
    var isImportant: Boolean = false,

    @PropertyName("priority")
    var priority: Int = 0 // Higher number = higher priority
) {
    // No-argument constructor required for Firestore
    constructor() : this("", "", "", "", "", "", "", 0, false, 0)
}


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

// PDF Notes Model
data class JetPdfNote(
    @PropertyName("pdfUrl")
    var pdfUrl: String = "",

    @PropertyName("subject")
    var subject: String = "",

    @PropertyName("topic")
    var topic: String = "",

    @PropertyName("pages")
    var pages: Int = 0,

    @PropertyName("fileSize")
    var fileSize: String = "",

    @PropertyName("language")
    var language: String = "Hindi/English"
) : JetMaterial() {
    constructor() : this("", "", "", 0, "", "Hindi/English")
}

// Video Model
data class JetVideo(
    @PropertyName("videoUrl")
    var videoUrl: String = "",

    @PropertyName("youtubeId")
    var youtubeId: String = "",

    @PropertyName("duration")
    var duration: String = "",

    @PropertyName("subject")
    var subject: String = "",

    @PropertyName("topic")
    var topic: String = "",

    @PropertyName("instructor")
    var instructor: String = "",

    @PropertyName("quality")
    var quality: String = "HD"
) : JetMaterial() {
    constructor() : this("", "", "", "", "", "", "HD")
}

// PYQ (Previous Year Question) Model
data class JetPyq(
    @PropertyName("pdfUrl")
    var pdfUrl: String = "",

    @PropertyName("year")
    var year: Int = 0,

    @PropertyName("examType")
    var examType: String = "", // "Main", "Advanced", "Practice"

    @PropertyName("totalQuestions")
    var totalQuestions: Int = 0,

    @PropertyName("duration")
    var duration: String = "",

    @PropertyName("hasAnswerKey")
    var hasAnswerKey: Boolean = false,

    @PropertyName("hasSolutions")
    var hasSolutions: Boolean = false,

    @PropertyName("solutionsUrl")
    var solutionsUrl: String = ""
) : JetMaterial() {
    constructor() : this("", 0, "", 0, "", false, false, "")
}

// Quiz Model
data class JetQuiz(
    @PropertyName("quizId")
    var quizId: String = "",

    @PropertyName("subject")
    var subject: String = "",

    @PropertyName("topic")
    var topic: String = "",

    @PropertyName("totalQuestions")
    var totalQuestions: Int = 0,

    @PropertyName("duration")
    var duration: Int = 0, // in minutes

    @PropertyName("difficulty")
    var difficulty: String = "", // "Easy", "Medium", "Hard"

    @PropertyName("totalMarks")
    var totalMarks: Int = 0,

    @PropertyName("attemptCount")
    var attemptCount: Int = 0,

    @PropertyName("averageScore")
    var averageScore: Float = 0f,

    @PropertyName("isLive")
    var isLive: Boolean = false
) : JetMaterial() {
    constructor() : this("", "", "", 0, 0, "", 0, 0, 0f, false)
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

// Mind Map Model
data class JetMindMap(
    @PropertyName("imageUrl")
    var imageUrl: String = "",

    @PropertyName("pdfUrl")
    var pdfUrl: String = "",

    @PropertyName("subject")
    var subject: String = "",

    @PropertyName("topic")
    var topic: String = "",

    @PropertyName("type")
    var type: String = "" // "Concept", "Formula", "Process", "Comparison"
) : JetMaterial() {
    constructor() : this("", "", "", "", "")
}

// Tips & Tricks Model
data class JetTip(
    @PropertyName("category")
    var category: String = "", // "Study Tips", "Exam Strategy", "Time Management", etc.

    @PropertyName("content")
    var content: String = "",

    @PropertyName("author")
    var author: String = "",

    @PropertyName("likes")
    var likes: Int = 0,

    @PropertyName("subject")
    var subject: String = "",

    @PropertyName("isVerified")
    var isVerified: Boolean = false
) : JetMaterial() {
    constructor() : this("", "", "", 0, "", false)
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