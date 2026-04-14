package com.example.tugas6.data.repository

import com.example.tugas6.data.model.Article
import com.example.tugas6.data.model.NewsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NewsRepository(private val httpClient: HttpClient) {
    private val apiKey = "f124b738bf26483c997b934cc672d803"
    private val baseUrl = "https://newsapi.org/v2"

    // Mengubah default ke "us" agar data pasti ada (NewsAPI id sering kosong)
    suspend fun getTopHeadlines(country: String = "us"): Result<List<Article>> {
        return try {
            val response: NewsResponse = httpClient.get("$baseUrl/top-headlines") {
                parameter("country", country)
                parameter("apiKey", apiKey)
            }.body()
            
            if (response.status == "ok") {
                Result.success(response.articles)
            } else {
                val errorMessage = response.message ?: "Unknown error"
                Result.failure(Exception("Error (${response.status}): $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
