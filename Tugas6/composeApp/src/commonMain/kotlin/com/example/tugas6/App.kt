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

// Definisi palet warna Hijau Soft & Putih
private val GreenSoftPrimary = Color(0xFF4CAF50)
private val GreenSoftSecondary = Color(0xFFC8E6C9)
private val GreenSoftBackground = Color(0xFFFAFAFA)
private val GreenSoftOnPrimary = Color.White
private val GreenSoftSurface = Color.White

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = GreenSoftPrimary,
        onPrimary = GreenSoftOnPrimary,
        primaryContainer = GreenSoftSecondary,
        onPrimaryContainer = Color(0xFF1B5E20),
        background = GreenSoftBackground,
        surface = GreenSoftSurface,
        onSurface = Color(0xFF212121),
        onBackground = Color(0xFF212121),
        secondary = Color(0xFF81C784),
        onSecondary = Color.White,
        error = Color(0xFFB00020),
        onError = Color.White,
        errorContainer = Color(0xFFFDE7E9),
        onErrorContainer = Color(0xFF601410)
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
