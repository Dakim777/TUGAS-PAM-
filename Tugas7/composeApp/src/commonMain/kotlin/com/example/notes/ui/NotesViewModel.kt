package com.example.notes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.db.Note
import com.example.notes.db.NoteRepository
import com.example.notes.settings.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class NotesUiState {
    object Loading : NotesUiState()
    object Empty : NotesUiState()
    data class Success(val notes: List<Note>) : NotesUiState()
}

class NotesViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<NotesUiState> = combine(
        _searchQuery.flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAllNotes()
            } else {
                repository.searchNotes(query)
            }
        },
        settingsManager.sortOrder,
        _searchQuery
    ) { notes, sortOrder, query ->
        if (notes.isEmpty()) {
            if (query.isBlank()) NotesUiState.Empty else NotesUiState.Success(emptyList())
        } else {
            val sortedNotes = when (sortOrder) {
                SettingsManager.SORT_DATE_ASC -> notes.sortedBy { it.updated_at }
                else -> notes.sortedByDescending { it.updated_at }
            }
            NotesUiState.Success(sortedNotes)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUiState.Loading
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun updateSortOrder(order: String) {
        viewModelScope.launch {
            settingsManager.updateSortOrder(order)
        }
    }
}
