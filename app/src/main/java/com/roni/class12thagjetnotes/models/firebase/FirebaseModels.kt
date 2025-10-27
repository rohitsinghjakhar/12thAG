package com.roni.class12thagjetnotes.models.firebase

// Video Model
data class Video(
    val id: String = "",
    val title: String = "",
    val subject: String = "",
    val chapter: String = "",
    val description: String = "",
    val thumbnailUrl: String = "",
    val videoUrl: String = "",
    val duration: String = "",
    val views: Int = 0,
    val instructor: String = "",
    val uploadedAt: Any? = null,
    val tags: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "", "", "", "", 0, "", null, emptyList())
}

// PDF Note Model
data class PdfNote(
    val id: String = "",
    val title: String = "",
    val subject: String = "",
    val chapter: String = "",
    val description: String = "",
    val pdfUrl: String = "",
    val thumbnailUrl: String = "",
    val pages: Int = 0,
    val size: String = "",
    val downloads: Int = 0,
    val uploadedAt: Any? = null,
    val category: String = "",
    val rating: Float = 0f,
    val tags: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "", "", "", 0, "", 0, null, "", 0f, emptyList())
}

// Book Model
data class Book(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val subject: String = "",
    val description: String = "",
    val coverImageUrl: String = "",
    val pdfUrl: String = "",
    val pages: Int = 0,
    val edition: String = "",
    val publisher: String = "",
    val rating: Float = 0f,
    val downloads: Int = 0,
    val uploadedAt: Any? = null,
    val isbn: String = "",
    val tags: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "", "", "", 0, "", "", 0f, 0, null, "", emptyList())
}

// Syllabus Model
data class Syllabus(
    val id: String = "",
    val title: String = "",
    val subject: String = "",
    val description: String = "",
    val pdfUrl: String = "",
    val topics: List<String> = emptyList(),
    val totalMarks: Int = 0,
    val duration: String = "",
    val examBoard: String = "",
    val year: Int = 0,
    val uploadedAt: Any? = null
) {
    constructor() : this("", "", "", "", "", emptyList(), 0, "", "", 0, null)
}

// Previous Paper Model (PYQ)
data class PreviousPaper(
    val id: String = "",
    val title: String = "",
    val year: String = "",
    val exam: String = "",
    val subject: String = "",
    val pdfUrl: String = "",
    val solutionUrl: String = "",
    val downloads: Int = 0,
    val uploadedAt: Any? = null,
    val totalQuestions: Int = 0,
    val totalMarks: Int = 0,
    val duration: Int = 0
) {
    constructor() : this("", "", "", "", "", "", "", 0, null, 0, 0, 0)
}

// Subject Model
data class Subject(
    val id: String = "",
    val name: String = "",
    val icon: String = "",
    val color: String = "",
    val description: String = "",
    val order: Int = 0
) {
    constructor() : this("", "", "", "", "", 0)
}


data class JetContent(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "", // "syllabus", "mock_test", "important_dates", "preparation_tips", "study_material"
    val url: String = "",
    val duration: String? = null,
    val questionsCount: Int = 0,
    val difficulty: String = "Medium"
)