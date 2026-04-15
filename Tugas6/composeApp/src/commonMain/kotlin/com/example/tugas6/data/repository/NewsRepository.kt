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

class NewsRepository(private val httpClient: HttpClient) {
    private val apiKey = "f124b738bf26483c997b934cc672d803"
    private val baseUrl = "https://newsapi.org/v2"
    
    // Inisialisasi local settings untuk caching
    private val settings: Settings = Settings()
    private val CACHE_KEY = "news_cache"

    suspend fun getTopHeadlines(country: String = "us"): Result<List<Article>> {
        return try {
            // 1. Coba ambil data dari Network
            val response: NewsResponse = httpClient.get("$baseUrl/top-headlines") {
                parameter("country", country)
                parameter("apiKey", apiKey)
            }.body()
            
            if (response.status == "ok") {
                // 2. Jika sukses, simpan ke cache (Bonus Fitur)
                saveToCache(response.articles)
                Result.success(response.articles)
            } else {
                // 3. Jika API error, coba ambil dari cache
                loadFromCache() ?: Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            // 4. Jika offline/no internet, ambil dari cache
            loadFromCache() ?: Result.failure(e)
        }
    }

    private fun saveToCache(articles: List<Article>) {
        try {
            val jsonString = Json.encodeToString(articles)
            settings[CACHE_KEY] = jsonString
        } catch (e: Exception) {
            // Gagal menyimpan cache tidak masalah bagi user
        }
    }

    private fun loadFromCache(): Result<List<Article>>? {
        val cachedData = settings.getStringOrNull(CACHE_KEY)
        return if (cachedData != null) {
            try {
                val articles = Json.decodeFromString<List<Article>>(cachedData)
                Result.success(articles)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
}
