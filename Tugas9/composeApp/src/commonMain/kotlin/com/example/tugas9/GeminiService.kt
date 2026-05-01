package com.example.tugas9

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class GeminiService(private val apiKey: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun generateContent(prompt: String): GeminiResponse {
        val response = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent") {
            url {
                parameters.append("key", apiKey)
            }
            contentType(ContentType.Application.Json)
            setBody(GeminiRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            ))
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw GeminiError.Unauthorized
            HttpStatusCode.TooManyRequests -> throw GeminiError.RateLimitExceeded
            else -> throw GeminiError.Unknown(response.status.value, "Error: ${response.status.description}")
        }
    }
}
