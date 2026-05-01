package com.example.tugas9

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // 1. Cek API Key di Logcat (Penting untuk memastikan key baru sudah masuk)
        // Cari tulisan "CEK_KEY" di Logcat kamu
        Log.d("CEK_KEY", "API Key yang terbaca: ${BuildConfig.API_KEY}")

        // 2. Inisialisasi Ktor HttpClient dengan konfigurasi paling "kebal"
        val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // Agar tidak error jika Google kirim data tambahan
                    isLenient = true         // Agar lebih fleksibel membaca format JSON
                    encodeDefaults = true
                })
            }
        }

        // 3. Inisialisasi Service dan Repository
        // Pastikan di GeminiService.kt kamu sudah menggunakan v1beta dan gemini-1.5-flash
        val geminiService = GeminiService(httpClient, BuildConfig.API_KEY)
        val aiRepository = AIRepository(geminiService)

        // 4. Inisialisasi ViewModel
        val viewModel: ChatViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return ChatViewModel(aiRepository) as T
                }
            }
        }

        setContent {
            App(viewModel)
        }
    }
}