package com.roni.class12thagjetnotes.models

data class OtherApp(
    val id: String,
    val name: String,
    val description: String,
    val icon: Int,
    val rating: Double,
    val downloads: String,
    val packageName: String,
    val playStoreUrl: String
)