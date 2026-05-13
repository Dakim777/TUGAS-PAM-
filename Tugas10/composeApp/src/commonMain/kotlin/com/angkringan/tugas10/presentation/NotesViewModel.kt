package com.angkringan.tugas10.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angkringan.tugas10.data.Note
import com.angkringan.tugas10.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NotesUiState {
    object Loading : NotesUiState()
    data class Success(val notes: List<Note>) : NotesUiState()
    data class Error(val message: String) : NotesUiState()
}

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.getAllNotes().collect { notes ->
                _uiState.value = NotesUiState.Success(notes)
            }
        }
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insertNote(Note(title = title, content = content))
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }
}