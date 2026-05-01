package com.example.tugas9

sealed class AIError(message: String) : Exception(message) {
    object Unauthorized : AIError("Autentikasi gagal. API Key tidak valid.")
    data class RateLimited(val retryAfterSeconds: Int) : AIError("Terlalu banyak permintaan. Coba lagi dalam $retryAfterSeconds detik.")
    data class Unknown(val code: Int, override val message: String) : AIError(message)
    data class NetworkError(override val message: String) : AIError(message)
}
