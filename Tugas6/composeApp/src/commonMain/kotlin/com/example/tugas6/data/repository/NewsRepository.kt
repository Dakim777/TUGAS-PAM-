package com.example.tugas6.data.repository

import com.example.tugas6.data.model.Article
import com.example.tugas6.data.model.NewsResponse
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class NewsData(
    val articles: List<Article>,
    val isFromCache: Boolean
)

class NewsRepository(private val httpClient: HttpClient) {
    private val apiKey = "f124b738bf26483c997b934cc672d803"
    private val baseUrl = "https://newsapi.org/v2"
    
    private val settings: Settings = Settings()
    private val CACHE_KEY = "news_cache"

    suspend fun getTopHeadlines(country: String = "us"): Result<NewsData> {
        return try {
            val response: NewsResponse = httpClient.get("$baseUrl/top-headlines") {
                parameter("country", country)
                parameter("apiKey", apiKey)
            }.body()
            
            if (response.status == "ok") {
                saveToCache(response.articles)
                Result.success(NewsData(response.articles, isFromCache = false))
            } else {
                val cached = loadFromCache()
                if (cached != null) {
                    Result.success(NewsData(cached, isFromCache = true))
                } else {
                    Result.failure(Exception(response.message))
                }
            }
        } catch (e: Exception) {
            val cached = loadFromCache()
            if (cached != null) {
                Result.success(NewsData(cached, isFromCache = true))
            } else {
                Result.failure(e)
            }
        }
    }

    private fun saveToCache(articles: List<Article>) {
        try {
            val jsonString = Json.encodeToString(articles)
            settings[CACHE_KEY] = jsonString
        } catch (e: Exception) {
        }
    }

    private fun loadFromCache(): List<Article>? {
        val cachedData = settings.getStringOrNull(CACHE_KEY)
        return if (cachedData != null) {
            try {
                Json.decodeFromString<List<Article>>(cachedData)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
}
