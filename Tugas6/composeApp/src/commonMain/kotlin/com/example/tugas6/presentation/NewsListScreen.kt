package com.example.tugas6.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tugas6.data.model.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("News Reader", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    }
                },
                actions = {
                    // Indikator Status di Top Bar
                    val isOffline = uiState is NewsUiState.Success && (uiState as NewsUiState.Success).isOffline
                    val isError = uiState is NewsUiState.Error
                    
                    val (statusText, statusColor, statusIcon) = when {
                        isOffline -> Triple("Offline", Color(0xFFE57373), Icons.Default.WifiOff)
                        isError -> Triple("No Connection", Color(0xFFE57373), Icons.Default.CloudOff)
                        uiState is NewsUiState.Success -> Triple("Online", Color(0xFF81C784), Icons.Default.Wifi)
                        else -> Triple("", Color.Transparent, null)
                    }

                    if (statusIcon != null) {
                        Surface(
                            color = statusColor.copy(alpha = 0.15f),
                            shape = CircleShape,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    statusIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = statusColor
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = statusText,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                    color = statusColor
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Banner Peringatan jika Offline
            if (uiState is NewsUiState.Success && (uiState as NewsUiState.Success).isOffline) {
                Surface(
                    color = Color(0xFFFFF3E0), // Orange muda untuk peringatan soft
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.CloudOff,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFFE65100)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Koneksi terputus. Menampilkan berita terakhir (Cache).",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFE65100)
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                when (val state = uiState) {
                    is NewsUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is NewsUiState.Success -> {
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = { viewModel.fetchTopHeadlines(isRefresh = true) },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(state.articles) { article ->
                                    NewsCard(
                                        article = article,
                                        onClick = { onArticleClick(article) }
                                    )
                                }
                            }
                        }
                    }
                    is NewsUiState.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.CloudOff,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Waduh, koneksi bermasalah!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.message,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(horizontal = 48.dp, vertical = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { viewModel.fetchTopHeadlines() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Coba Lagi", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
