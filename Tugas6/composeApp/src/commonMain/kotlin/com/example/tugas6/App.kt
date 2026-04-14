package com.example.tugas6

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import com.example.tugas6.data.network.HttpClientFactory
import com.example.tugas6.data.repository.NewsRepository
import com.example.tugas6.presentation.NewsListScreen
import com.example.tugas6.presentation.NewsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val httpClient = remember { HttpClientFactory.create() }
    val repository = remember { NewsRepository(httpClient) }
    val viewModel = remember { NewsViewModel(repository) }
    val uriHandler = LocalUriHandler.current

    MaterialTheme {
        NewsListScreen(
            viewModel = viewModel,
            onArticleClick = { article ->
                // Navigasi sederhana: Buka URL artikel di browser/web view sistem
                uriHandler.openUri(article.url)
            }
        )
    }
}
