package com.example.tugas9

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.pow

class ChatViewModel(private val repository: AIRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(text: String) {
        val trimmedText = text.trim()
        if (trimmedText.isBlank() || _uiState.value.isLoading) return

        // Tambahkan pesan user ke UI
        val userMessage = ChatMessage(text = trimmedText, isUser = true)
        _uiState.update { 
            it.copy(
                messages = it.messages + userMessage,
                isLoading = true,
                errorMessage = null
            )
        }

        // Kirim seluruh riwayat pesan agar AI punya konteks
        executeWithHistory()
    }

    private fun executeWithHistory(maxRetries: Int = 2) {
        viewModelScope.launch {
            var currentRetry = 0
            var succeeded = false
            val currentHistory = _uiState.value.messages

            while (currentRetry <= maxRetries && !succeeded) {
                try {
                    val result = repository.sendMessage(currentHistory)
                    result.onSuccess { botResponse ->
                        _uiState.update { 
                            it.copy(
                                messages = it.messages + ChatMessage(text = botResponse, isUser = false),
                                isLoading = false
                            )
                        }
                        succeeded = true
                    }.onFailure { error ->
                        throw error 
                    }
                } catch (e: Exception) {
                    if (e is GeminiError.RateLimitExceeded && currentRetry < maxRetries) {
                        val waitTime = 2.0.pow(currentRetry).toLong() * 2000
                        delay(waitTime)
                        currentRetry++
                    } else {
                        val message = when (e) {
                            is GeminiError.Unauthorized -> "Konfigurasi API bermasalah. Periksa kunci API Anda."
                            is GeminiError.RateLimitExceeded -> "Terlalu sibuk. Tunggu sebentar ya..."
                            is GeminiError.NetworkError -> "Koneksi terputus. Coba cek internetmu."
                            else -> "Waduh, ada kendala: ${e.message ?: "Error teknis"}"
                        }
                        _uiState.update { 
                            it.copy(isLoading = false, errorMessage = message) 
                        }
                        break
                    }
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
