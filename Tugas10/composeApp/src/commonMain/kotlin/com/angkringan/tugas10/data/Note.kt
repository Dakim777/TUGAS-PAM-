package com.angkringan.tugas10.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.MutableStateFlow

data class Note(val id: Long = 0, val title: String, val content: String)

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(id: Long)
}

class NoteDatabase {

}

class NoteRepositoryImpl(private val db: NoteDatabase) : NoteRepository {
    private val notes = mutableListOf<Note>()
    private val _notesFlow = MutableStateFlow<List<Note>>(emptyList())

    override fun getAllNotes(): Flow<List<Note>> = _notesFlow

    override suspend fun insertNote(note: Note) {
        notes.add(note)
        _notesFlow.value = notes.toList()
    }

    override suspend fun deleteNote(id: Long) {
        notes.removeAll { it.id == id }
        _notesFlow.value = notes.toList()
    }
}