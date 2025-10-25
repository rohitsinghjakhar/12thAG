package com.roni.class12thagjetnotes.models

data class DeveloperProfile(
    val id: String = "",
    val name: String = "",
    val title: String = "",
    val tagline: String = "",
    val photoUrl: String = "",
    val bio: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val experience: String = "",
    val skills: List<String> = emptyList(),
    val github: String = "",
    val linkedin: String = "",
    val portfolio: String = "",
    val education: String = "",
    val languages: List<String> = emptyList(),
    val achievements: List<String> = emptyList()
) {
    // No-arg constructor for Firebase
    constructor() : this("", "", "", "", "", "", "", "", "", "", emptyList(), "", "", "", "", emptyList(), emptyList())
}