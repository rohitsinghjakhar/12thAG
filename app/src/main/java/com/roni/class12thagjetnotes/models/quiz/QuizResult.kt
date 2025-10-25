package com.roni.class12thagjetnotes.models.quiz

data class QuizResult(
    val id: String = "",
    val userId: String = "",
    val quizId: String = "",
    val quizTitle: String = "",
    val score: Int = 0,
    val maxScore: Int = 0,
    val percentage: Double = 0.0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val unattempted: Int = 0,
    val timeTaken: Int = 0, // in minutes
    val completedAt: Any? = null,
    val answers: Map<String, Int> = emptyMap() // questionId to selected option index
) {
    constructor() : this("", "", "", "", 0, 0, 0.0, 0, 0, 0, 0, null, emptyMap())
}