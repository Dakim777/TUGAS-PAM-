package com.example.newsfeeddaffa

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class RawNews(val id: Int, val title: String, val category: String, val timestamp: Long)

data class NewsUiModel(
    val id: Int,
    val displayTitle: String,
    val categoryColor: Color,
    val timeFormatted: String,
    val category: String,
    var isRead: Boolean = false,
    var detailContent: String? = null,
    var isLoading: Boolean = false,
    var isExpanded: Boolean = false
)

class NewsRepository {
    private var counter = 0
    private val categories = listOf("Teknologi", "Olahraga", "Bisnis", "Politik", "Kesehatan")

    val newsStream: Flow<RawNews> = flow {
        while (true) {
            delay(2500)
            counter++
            val randomCategory = categories.random()
            val titles = listOf(
                "Inovasi Terbaru di Sektor $randomCategory",
                "Tren Global $randomCategory Tahun 2026",
                "Laporan Khusus: Perkembangan $randomCategory",
                "Pendapat Ahli Mengenai $randomCategory",
                "Update Strategis Industri $randomCategory"
            )
            emit(RawNews(id = counter, title = titles.random() + " #$counter", category = randomCategory, timestamp = System.currentTimeMillis()))
        }
    }

    suspend fun fetchNewsDetail(id: Int): String {
        delay(1000)
        return "Detail lengkap berita #$id: Berita ini merangkum perkembangan terbaru yang terjadi secara signifikan. Analisis menunjukkan bahwa dampak jangka panjang akan mulai terlihat dalam beberapa bulan ke depan."
    }
}

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    private val _newsList = MutableStateFlow<List<NewsUiModel>>(emptyList())
    val newsList: StateFlow<List<NewsUiModel>> = _newsList.asStateFlow()
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()
    private val _activeFilter = MutableStateFlow<String?>(null)
    val activeFilter: StateFlow<String?> = _activeFilter.asStateFlow()

    init {
        viewModelScope.launch {
            repository.newsStream.map { raw ->
                NewsUiModel(
                    id = raw.id,
                    displayTitle = raw.title,
                    categoryColor = when(raw.category) {
                        "Teknologi" -> Color(0xFF3F51B5)
                        "Olahraga" -> Color(0xFFE91E63)
                        "Bisnis" -> Color(0xFF009688)
                        "Politik" -> Color(0xFFFF9800)
                        else -> Color(0xFF4CAF50)
                    },
                    timeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(raw.timestamp)),
                    category = raw.category
                )
            }.collect { newItem -> _newsList.update { listOf(newItem) + it } }
        }
    }

    fun setFilter(category: String?) { _activeFilter.value = category }

    fun onNewsClicked(newsItem: NewsUiModel) {
        if (newsItem.isLoading) return
        if (newsItem.detailContent != null) {
            updateItem(newsItem.id) { it.copy(isExpanded = !it.isExpanded) }
            return
        }
        viewModelScope.launch {
            updateItem(newsItem.id) { it.copy(isLoading = true) }
            val detail = repository.fetchNewsDetail(newsItem.id)
            updateItem(newsItem.id) { it.copy(isLoading = false, isRead = true, detailContent = detail, isExpanded = true) }
            if (!newsItem.isRead) _readCount.update { it + 1 }
        }
    }

    private fun updateItem(id: Int, update: (NewsUiModel) -> NewsUiModel) {
        _newsList.update { list -> list.map { if (it.id == id) update(it) else it } }
    }
}

@Composable
fun NewsFeedScreen(viewModel: NewsViewModel = viewModel()) {
    val allNews by viewModel.newsList.collectAsState()
    val readCount by viewModel.readCount.collectAsState()
    val activeFilter by viewModel.activeFilter.collectAsState()
    val displayedNews = remember(allNews, activeFilter) {
        if (activeFilter == null) allNews else allNews.filter { it.category == activeFilter }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        topBar = {
            Surface(shadowElevation = 3.dp) {
                Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Column {
                        Text("NewsFeed Daffa", fontSize = 22.sp, fontWeight = FontWeight.Black)
                        Text("Simulator Berita Real-time", fontSize = 12.sp, color = Color.Gray)
                    }
                    Surface(color = Color(0xFFE3F2FD), shape = RoundedCornerShape(16.dp)) {
                        Text("$readCount Dibaca", Modifier.padding(8.dp, 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
                    }
                }
            }
        }
    ) { pv ->
        Column(Modifier.padding(pv).fillMaxSize().background(Color(0xFFF8F9FA))) {
            Row(Modifier.fillMaxWidth().padding(vertical = 12.dp).horizontalScroll(rememberScrollState())) {
                Spacer(Modifier.width(16.dp))
                FilterChipItem("Semua", activeFilter == null) { viewModel.setFilter(null) }
                listOf("Teknologi", "Olahraga", "Bisnis", "Politik", "Kesehatan").forEach {
                    FilterChipItem(it, activeFilter == it) { viewModel.setFilter(it) }
                }
                Spacer(Modifier.width(16.dp))
            }

            if (displayedNews.isEmpty()) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(16.dp)) {
                    items(displayedNews, key = { it.id }) { news ->
                        NewsCardItem(news) { viewModel.onNewsClicked(news) }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipItem(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        Modifier.padding(end = 8.dp).clickable { onClick() },
        color = if (selected) Color.Black else Color.White,
        shape = RoundedCornerShape(20.dp),
        border = if (selected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
    ) {
        Text(label, Modifier.padding(16.dp, 8.dp), fontSize = 12.sp, color = if (selected) Color.White else Color.Black)
    }
}

@Composable
fun NewsCardItem(news: NewsUiModel, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Brush.linearGradient(listOf(news.categoryColor, news.categoryColor.copy(0.6f)))), Alignment.Center) {
                    Text(news.category.first().toString(), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(news.category.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = news.categoryColor)
                    Text(news.timeFormatted, fontSize = 10.sp, color = Color.Gray)
                }
                if (!news.isRead) Icon(Icons.Default.Refresh, null, Modifier.size(16.dp), Color.Red)
                else Icon(Icons.Default.Check, null, Modifier.size(16.dp), Color(0xFF4CAF50))
            }
            Spacer(Modifier.height(8.dp))
            Text(news.displayTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = if(news.isRead) Color.Gray else Color.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)

            if (news.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth().padding(top = 8.dp).height(2.dp), news.categoryColor)

            AnimatedVisibility(news.isExpanded && news.detailContent != null) {
                Column {
                    HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                    Text(news.detailContent ?: "", fontSize = 13.sp, lineHeight = 18.sp, color = Color.DarkGray)
                }
            }
        }
    }
}