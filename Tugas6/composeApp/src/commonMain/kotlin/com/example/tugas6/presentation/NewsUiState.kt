package com.example.tugas6.presentation

import com.example.tugas6.data.model.Article

sealed interface NewsUiState {
    data object Loading : NewsUiState
    data class Success(
        val articles: List<Article>,
        val isOffline: Boolean = false
    ) : NewsUiState
    data class Error(val message: String) : NewsUiState
}
