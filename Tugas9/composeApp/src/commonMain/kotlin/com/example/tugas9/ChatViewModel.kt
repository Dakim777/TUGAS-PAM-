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
        if (text.isBlank()) return

        val userMessage = ChatMessage(text = text, isUser = true)
        _uiState.update { 
            it.copy(
                messages = it.messages + userMessage,
                isLoading = true,
                errorMessage = null
            )
        }

        executeWithRetry(text)
    }

    private fun executeWithRetry(prompt: String, maxRetries: Int = 3) {
        viewModelScope.launch {
            var currentRetry = 0
            var succeeded = false

            while (currentRetry <= maxRetries && !succeeded) {
                try {
                    val result = repository.sendMessage(prompt)
                    result.onSuccess { botResponse ->
                        _uiState.update { 
                            it.copy(
                                messages = it.messages + ChatMessage(text = botResponse, isUser = false),
                                isLoading = false
                            )
                        }
                        succeeded = true
                    }.onFailure { error ->
                        throw error // Rethrow to handle in catch block for retry logic
                    }
                } catch (e: Exception) {
                    if (e is GeminiError.RateLimitExceeded && currentRetry < maxRetries) {
                        val waitTime = 2.0.pow(currentRetry).toLong() * 1000
                        delay(waitTime)
                        currentRetry++
                    } else {
                        val message = when (e) {
                            is GeminiError.Unauthorized -> "Error: Api Key tidak valid."
                            is GeminiError.RateLimitExceeded -> "Error: Terlalu banyak permintaan. Coba lagi nanti."
                            else -> "Terjadi kesalahan: ${e.message}"
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
