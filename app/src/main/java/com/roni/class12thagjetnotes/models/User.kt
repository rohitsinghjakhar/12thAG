package com.roni.class12thagjetnotes.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val profilePictureUrl: String = "",
    val createdAt: Any? = null,
    val savedNotes: List<String> = emptyList(),
    val completedQuizzes: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "", null, emptyList(), emptyList())
}