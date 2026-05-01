package com.example.tugas9

sealed class GeminiError(message: String) : Exception(message) {
    object Unauthorized : GeminiError("API Key tidak valid atau tidak diizinkan.")
    object RateLimitExceeded : GeminiError("Terlalu banyak permintaan. Silakan coba lagi nanti.")
    data class Unknown(val code: Int, override val message: String) : GeminiError(message)
    data class NetworkError(override val message: String) : GeminiError(message)
}
