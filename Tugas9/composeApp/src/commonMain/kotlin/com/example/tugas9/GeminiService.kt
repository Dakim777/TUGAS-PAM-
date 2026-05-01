package com.example.tugas9

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Service layer untuk berinteraksi dengan Google Gemini API.
 * Mengimplementasikan Ktor Client dengan ContentNegotiation JSON.
 */
class GeminiService(private val apiKey: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = false
                explicitNulls = false // Mencegah field null ikut terkirim
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 60000
        }
    }

    suspend fun generateContent(contents: List<Content>, systemInstruction: String? = null): GeminiResponse {
        val cleanApiKey = apiKey.trim()
        
        if (cleanApiKey.isBlank() || cleanApiKey.startsWith("YOUR_API") || cleanApiKey == "null") {
            throw GeminiError.Unauthorized
        }
        
        return try {
            // Menggunakan v1beta karena fitur system_instruction lebih stabil di sini
            val response = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent") {
                url { parameters.append("key", cleanApiKey) }
                contentType(ContentType.Application.Json)
                setBody(GeminiRequest(
                    contents = contents,
                    // PENTING: Untuk system_instruction, kita kirim object Content TANPA field role.
                    systemInstruction = systemInstruction?.let {
                        Content(parts = listOf(Part(text = it)), role = null)
                    }
                ))
            }

            if (response.status == HttpStatusCode.OK) {
                response.body()
            } else {
                val errorBody = response.bodyAsText()
                println("GEMINI ERROR DETAIL: $errorBody")
                
                when (response.status) {
                    HttpStatusCode.Unauthorized -> throw GeminiError.Unauthorized
                    HttpStatusCode.TooManyRequests -> throw GeminiError.RateLimitExceeded
                    else -> {
                        // Memperbaiki pesan error agar lebih informatif sesuai rubrik
                        throw GeminiError.Unknown(response.status.value, "API Error ${response.status.value}: $errorBody")
                    }
                }
            }
        } catch (e: Exception) {
            if (e is GeminiError) throw e
            throw GeminiError.NetworkError("Koneksi gagal: ${e.message}")
        }
    }
}
