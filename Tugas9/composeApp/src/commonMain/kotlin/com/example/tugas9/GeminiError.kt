package com.example.tugas9

sealed class AIError : Exception() {
    // Kata 'val' di sini wajib ada agar bisa dipanggil di Repository
    data class RateLimited(val retryAfter: Int) : AIError()
    data class Unauthorized(override val message: String) : AIError()
    data class ServerError(override val message: String) : AIError()
    data class NetworkError(override val message: String) : AIError()
    data class ParseError(override val message: String) : AIError()
    data class UnknownError(override val message: String) : AIError()
}