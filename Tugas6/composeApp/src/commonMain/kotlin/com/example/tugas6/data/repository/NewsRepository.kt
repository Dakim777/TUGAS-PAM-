package com.example.tugas6.data.repository

import com.example.tugas6.data.model.Article
import com.example.tugas6.data.model.NewsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NewsRepository(private val httpClient: HttpClient) {
    private val apiKey = "YOUR_NEWS_API_KEY" // Ganti dengan API Key Anda
    private val baseUrl = "https://newsapi.org/v2"

    suspend fun getTopHeadlines(country: String = "us"): Result<List<Article>> {
        return try {
            val response: NewsResponse = httpClient.get("$baseUrl/top-headlines") {
                parameter("country", country)
                parameter("apiKey", apiKey)
            }.body()
            
            if (response.status == "ok") {
                Result.success(response.articles)
            } else {
                Result.failure(Exception("Error fetching news: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
