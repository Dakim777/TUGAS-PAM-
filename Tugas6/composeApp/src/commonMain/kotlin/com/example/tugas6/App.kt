package com.example.tugas6

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.tugas6.data.model.Article
import com.example.tugas6.data.network.HttpClientFactory
import com.example.tugas6.data.repository.NewsRepository
import com.example.tugas6.presentation.NewsDetailScreen
import com.example.tugas6.presentation.NewsListScreen
import com.example.tugas6.presentation.NewsViewModel

@Composable
fun App() {
    val httpClient = remember { HttpClientFactory.create() }
    val repository = remember { NewsRepository(httpClient) }
    val viewModel = remember { NewsViewModel(repository) }
    
    // Sederhana: Gunakan State untuk navigasi antar screen
    var selectedArticle by remember { mutableStateOf<Article?>(null) }

    MaterialTheme {
        if (selectedArticle == null) {
            NewsListScreen(
                viewModel = viewModel,
                onArticleClick = { article ->
                    selectedArticle = article
                }
            )
        } else {
            NewsDetailScreen(
                article = selectedArticle!!,
                onBackClick = { selectedArticle = null }
            )
        }
    }
}
