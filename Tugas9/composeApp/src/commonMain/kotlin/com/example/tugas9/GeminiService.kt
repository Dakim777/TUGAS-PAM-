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

class GeminiService(private val apiKey: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = false
                explicitNulls = false
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 30000
        }
    }

    // Mengembalikan Result<GeminiResponse> untuk penanganan error yang lebih baik di Repository
    suspend fun generateContent(contents: List<Content>, systemInstructionText: String? = null): Result<GeminiResponse> {
        val cleanApiKey = apiKey.trim().removeSurrounding("\"")
        
        if (cleanApiKey.isBlank() || cleanApiKey.contains("YOUR_API")) {
            return Result.failure(AIError.Unauthorized)
        }

        return try {
            val response = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent") {
                url { parameters.append("key", cleanApiKey) }
                contentType(ContentType.Application.Json)
                setBody(GeminiRequest(
                    contents = contents,
                    systemInstruction = systemInstructionText?.let { 
                        SystemContent(parts = listOf(Part(text = it)))
                    }
                ))
            }

            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body())
            } else {
                val errorBody = response.bodyAsText()
                println("GEMINI ERROR: ${response.status} - $errorBody")
                when (response.status) {
                    HttpStatusCode.Unauthorized -> Result.failure(AIError.Unauthorized)
                    HttpStatusCode.TooManyRequests -> {
                        // Coba baca header Retry-After untuk info durasi delay
                        val retryAfter = response.headers["Retry-After"]?.toIntOrNull() ?: 60 // Default 60 detik jika header tidak ada
                        Result.failure(AIError.RateLimited(retryAfter))
                    }
                    else -> Result.failure(AIError.Unknown(response.status.value, "API Error: $errorBody"))
                }
            }
        } catch (e: Exception) {
            // Menangkap error jaringan atau Ktor
            Result.failure(AIError.NetworkError(e.message ?: "Error jaringan tidak diketahui"))
        }
    }
}
