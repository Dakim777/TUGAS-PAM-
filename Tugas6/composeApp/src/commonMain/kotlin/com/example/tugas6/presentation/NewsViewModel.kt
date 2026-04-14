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

    init {
        fetchTopHeadlines()
    }

    fun fetchTopHeadlines() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            repository.getTopHeadlines()
                .onSuccess { articles ->
                    _uiState.value = NewsUiState.Success(articles)
                }
                .onFailure { exception ->
                    _uiState.value = NewsUiState.Error(exception.message ?: "Unknown Error")
                }
        }
    }
}
