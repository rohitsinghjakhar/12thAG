package com.roni.class12thagjetnotes.models.quiz

data class QuizDetail(
    val id: String = "",
    val title: String = "",
    val subject: String = "",
    val duration: Int = 0,
    val maxMarks: Int = 0,
    val questions: List<Question> = emptyList()
) {
    constructor() : this("", "", "", 0, 0, emptyList())
}