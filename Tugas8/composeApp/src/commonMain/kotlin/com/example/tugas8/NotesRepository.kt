package com.example.tugas8

import androidx.compose.runtime.mutableStateListOf

class NotesRepository {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> get() = _notes
    
    fun addNote(title: String, content: String) {
        _notes.add(Note(System.currentTimeMillis(), title, content))
    }
    
    fun deleteNote(id: Long) {
        _notes.removeAll { it.id == id }
    }
    
    fun updateNote(id: Long, title: String, content: String) {
        val index = _notes.indexOfFirst { it.id == id }
        if (index != -1) {
            _notes[index] = Note(id, title, content)
        }
    }
}
