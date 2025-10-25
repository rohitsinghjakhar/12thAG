package com.roni.class12thagjetnotes.models.quiz

data class Quiz(
    val id: String = "",
    val title: String = "",
    val subject: String = "",
    val description: String = "",
    val totalQuestions: Int = 0,
    val duration: Int = 0, // in minutes
    val maxMarks: Int = 0,
    val difficulty: String = "", // Easy, Medium, Hard
    val attempts: Int = 0,
    val thumbnailUrl: String = "",
    val tags: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", 0, 0, 0, "", 0, "", emptyList())
}