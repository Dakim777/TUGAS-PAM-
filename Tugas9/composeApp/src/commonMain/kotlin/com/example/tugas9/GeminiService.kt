package com.example.tugas9

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class GeminiService(
    private val client: HttpClient,
    private val apiKey: String
) {
    // Jalur paling
    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val modelName = "gemini-2.5-flash-lite"

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun generateContent(contents: List<Content>): Result<GeminiResponse> {
        return try {
            val request = GeminiRequest(contents = contents)

            // Kita print URL-nya biar lu bisa liat di Logcat bener apa nggak
            val fullUrl = "$baseUrl/models/$modelName:generateContent"
            println("=== DEBUG URL: $fullUrl ===")

            val httpResponse = client.post(fullUrl) {
                contentType(ContentType.Application.Json)
                parameter("key", apiKey)
                setBody(request)
            }

            val rawJson = httpResponse.bodyAsText()

            // CCTV biar kita tau Google ngomong apa
            println("=== CCTV RESULT ===")
            println(rawJson)

            val response = jsonParser.decodeFromString<GeminiResponse>(rawJson)
            Result.success(response)

        } catch (e: Exception) {
            println("=== CCTV ERROR: ${e.message} ===")
            Result.failure(e)
        }
    }
}