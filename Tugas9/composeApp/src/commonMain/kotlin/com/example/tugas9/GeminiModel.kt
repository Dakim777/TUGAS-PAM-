package com.example.tugas9

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    @SerialName("candidates")
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    @SerialName("content")
    val content: Content? = null,
    @SerialName("finishReason")
    val finishReason: String? = null
)

@Serializable
data class Content(
    @SerialName("parts")
    val parts: List<Part>? = null,
    @SerialName("role")
    val role: String? = null
)

@Serializable
data class Part(
    @SerialName("text")
    val text: String? = null
)

@Serializable
data class GeminiRequest(
    @SerialName("contents")
    val contents: List<Content>
)