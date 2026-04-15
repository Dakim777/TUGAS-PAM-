package com.example.tugas6

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.tugas6.data.model.Article
import com.example.tugas6.data.network.HttpClientFactory
import com.example.tugas6.data.repository.NewsRepository
import com.example.tugas6.presentation.NewsDetailScreen
import com.example.tugas6.presentation.NewsListScreen
import com.example.tugas6.presentation.NewsViewModel

// Definisi palet warna Biru Muda & Putih
private val BlueLightPrimary = Color(0xFF007BFF)
private val BlueLightSecondary = Color(0xFFE3F2FD)
private val BlueLightBackground = Color(0xFFF8F9FA)
private val BlueLightOnPrimary = Color.White
private val BlueLightSurface = Color.White

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = BlueLightPrimary,
        onPrimary = BlueLightOnPrimary,
        primaryContainer = BlueLightSecondary,
        onPrimaryContainer = BlueLightPrimary,
        background = BlueLightBackground,
        surface = BlueLightSurface,
        onSurface = Color.Black,
        onBackground = Color.Black
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
fun App() {
    val httpClient = remember { HttpClientFactory.create() }
    val repository = remember { NewsRepository(httpClient) }
    val viewModel = remember { NewsViewModel(repository) }
    
    var selectedArticle by remember { mutableStateOf<Article?>(null) }

    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
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
}
