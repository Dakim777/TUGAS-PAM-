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

        val userMessage = ChatMessage(text = trimmedText, isUser = true)
        _uiState.update { 
            it.copy(
                messages = it.messages + userMessage,
                isLoading = true,
                errorMessage = null
            )
        }

        executeTranslation()
    }

    private fun executeTranslation() {
        viewModelScope.launch {
            val lastUserMessage = _uiState.value.messages.lastOrNull { it.isUser }?.text ?: return@launch

            try {
                val result = repository.translateText(lastUserMessage)
                result.onSuccess { botResponse ->
                    _uiState.update { 
                        it.copy(
                            messages = it.messages + ChatMessage(text = botResponse, isUser = false),
                            isLoading = false
                        )
                    }
                }.onFailure { error ->
                    println("ChatViewModel Error AI: ${error.message}") // Logging error
                    val message = when (error) {
                        is AIError.Unauthorized -> "Autentikasi gagal. Cek API Key Anda."
                        is AIError.RateLimited -> error.message // Pesan dari AIError.RateLimited
                        is AIError.NetworkError -> "Koneksi internet bermasalah: ${error.message}"
                        is AIError.Unknown -> "Terjadi kendala: ${error.message}"
                        else -> "Terjadi kendala tidak diketahui: ${error.message}"
                    }
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = message) 
                    }
                }
            } catch (e: Exception) {
                // Ini akan menangkap error yang tidak ditangani oleh onFaliure (misal Exception dari retryWithBackoff)
                println("ChatViewModel Catch All Error: ${e.message}")
                _uiState.update { 
                    it.copy(isLoading = false, errorMessage = e.message ?: "Terjadi kesalahan fatal.")
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
