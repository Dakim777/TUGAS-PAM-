package com.example.tugas9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Inisialisasi dependensi secara manual
        val geminiService = GeminiService(BuildConfig.API_KEY)
        val aiRepository = AIRepository(geminiService)
        
        // Menggunakan Factory untuk menyuntikkan repository ke ViewModel
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
