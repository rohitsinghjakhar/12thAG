package com.roni.class12thagjetnotes.models
data class Review(
    val id: String = "",
    val studentName: String = "",
    val studentCourse: String = "",
    val rating: Float = 0f,
    val reviewText: String = "",
    val date: String = "",
    val userImageUrl: String = ""
) {
    constructor() : this("", "", "", 0f, "", "", "")
}