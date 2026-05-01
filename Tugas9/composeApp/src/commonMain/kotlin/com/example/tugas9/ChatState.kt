package com.example.tugas9

import kotlinx.serialization.Serializable

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@Serializable
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = 0L // Bisa ditambahkan jika perlu
)
