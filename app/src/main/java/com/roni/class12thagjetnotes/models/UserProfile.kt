package com.roni.class12thagjetnotes.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.util.Date

data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val studentId: String = "",
    val department: String = "",
    val yearOfStudy: Int = 0,
    val address: String = "",
    val photoUrl: String = "",

    // Use Any? to accept both Long and Timestamp
    @get:PropertyName("createdAt")
    @set:PropertyName("createdAt")
    var createdAt: Any? = null,

    @get:PropertyName("updatedAt")
    @set:PropertyName("updatedAt")
    var updatedAt: Any? = null
) {
    // No-arg constructor for Firebase
    constructor() : this("", "", "", "", "", "", 0, "", "", null, null)

    // Helper function to get createdAt as Long (milliseconds)
    fun getCreatedAtMillis(): Long {
        return when (createdAt) {
            is Timestamp -> (createdAt as Timestamp).toDate().time
            is Long -> createdAt as Long
            else -> 0L
        }
    }

    // Helper function to get updatedAt as Long (milliseconds)
    fun getUpdatedAtMillis(): Long {
        return when (updatedAt) {
            is Timestamp -> (updatedAt as Timestamp).toDate().time
            is Long -> updatedAt as Long
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

    // Helper function to get updatedAt as Timestamp
    fun getUpdatedAtTimestamp(): Timestamp? {
        return when (updatedAt) {
            is Timestamp -> updatedAt as Timestamp
            is Long -> Timestamp(Date(updatedAt as Long))
            else -> null
        }
    }
}