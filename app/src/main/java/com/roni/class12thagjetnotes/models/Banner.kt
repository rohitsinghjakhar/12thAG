package com.roni.class12thagjetnotes.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.util.Date

data class Banner(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val imageUrl: String = "",
    val isActive: Boolean = true,
    val order: Int = 0,

    // Use Any? to accept both Long and Timestamp
    @get:PropertyName("createdAt")
    @set:PropertyName("createdAt")
    var createdAt: Any? = null
) {
    // No-arg constructor for Firebase
    constructor() : this("", "", "", "", true, 0, null)

    // Helper function to get createdAt as Long (milliseconds)
    fun getCreatedAtMillis(): Long {
        return when (createdAt) {
            is Timestamp -> (createdAt as Timestamp).toDate().time
            is Long -> createdAt as Long
            else -> 0L
        }
    }

    // Helper function to get createdAt as Timestamp
    fun getCreatedAtTimestamp(): Timestamp? {
        return when (createdAt) {
            is Timestamp -> createdAt as Timestamp
            is Long -> Timestamp(Date(createdAt as Long))
            else -> null
        }
    }
}