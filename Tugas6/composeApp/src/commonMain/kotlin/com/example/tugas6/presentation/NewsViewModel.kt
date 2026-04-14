package com.example.tugas6.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugas6.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        fetchTopHeadlines()
    }

    fun fetchTopHeadlines(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _isRefreshing.value = true
            } else {
                _uiState.value = NewsUiState.Loading
            }

            try {
                repository.getTopHeadlines()
                    .onSuccess { articles ->
                        // Langsung success saja walaupun kosong agar tidak muncul pesan error merah
                        _uiState.value = NewsUiState.Success(articles)
                    }
                    .onFailure { exception ->
                        _uiState.value = NewsUiState.Error(exception.message ?: "Gagal mengambil data.")
                    }
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error("Terjadi kesalahan sistem: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
