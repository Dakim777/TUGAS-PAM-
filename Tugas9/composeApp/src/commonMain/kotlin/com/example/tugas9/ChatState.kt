package com.example.tugas9

import kotlinx.serialization.Serializable
import com.benasher44.uuid.uuid4

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@Serializable
data class ChatMessage(
    val id: String = uuid4().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = 0L
)
