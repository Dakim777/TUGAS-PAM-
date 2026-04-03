package com.example.tugas5.model

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val date: Long = 0L
)
