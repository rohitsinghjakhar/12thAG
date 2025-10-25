package com.roni.class12thagjetnotes.models.quiz

data class Question(
    val questionId: String = "",
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: Int = 0, // Index of correct option
    val marks: Int = 4,
    val negativeMarks: Int = -1,
    val explanation: String = "",
    val imageUrl: String = ""
) {
    constructor() : this("", "", emptyList(), 0, 4, -1, "", "")
}