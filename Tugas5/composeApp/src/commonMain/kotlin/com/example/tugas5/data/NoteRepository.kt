package com.example.tugas5.data

import com.example.tugas5.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteRepository {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        // Menggunakan full path agar tidak bentrok dengan kotlin.time.Clock
        val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        _notes.value = listOf(
            Note("1", "Tugas PAM", "Mengerjakan tugas navigasi minggu ke-5", isFavorite = true, date = now),
            Note("2", "Belanja", "Beli susu, telur, dan kopi", isFavorite = false, date = now),
            Note("3", "Ide Project", "Membuat aplikasi pengingat minum air", isFavorite = true, date = now)
        )
    }

    fun getNoteById(id: String): Note? {
        return _notes.value.find { it.id == id }
    }

    fun addNote(title: String, content: String) {
        val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        val newNote = Note(
            id = now.toString(),
            title = title,
            content = content,
            date = now
        )
        _notes.value += newNote
    }

    fun updateNote(id: String, title: String, content: String) {
        _notes.value = _notes.value.map {
            if (it.id == id) it.copy(title = title, content = content) else it
        }
    }

    fun toggleFavorite(id: String) {
        _notes.value = _notes.value.map {
            if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun deleteNote(id: String) {
        _notes.value = _notes.value.filter { it.id != id }
    }
}
