package com.roni.class12thagjetnotes.models.jet

data class JetResource(
    val id: String = "",
    val category: String = "",
    val title: String = "",
    val content: String = "",
    val data: Map<String, Any> = emptyMap(),
    val updatedAt: Any? = null
) {
    constructor() : this("", "", "", "", emptyMap(), null)
}