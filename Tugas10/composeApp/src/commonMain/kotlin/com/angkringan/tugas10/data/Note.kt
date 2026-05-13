package com.angkringan.tugas10.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

// Entity dasar
data class Note(val id: Long = 0, val title: String, val content: String)

// Interface Repository (Sangat penting untuk Mocking di Testing)
interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(id: Long)
}

// Dummy Database (Untuk simulasi Koin DI)
class NoteDatabase {
    // Simulasi in-memory database
}

// Implementasi Repository
class NoteRepositoryImpl(private val db: NoteDatabase) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = emptyFlow()
    override suspend fun insertNote(note: Note) {}
    override suspend fun deleteNote(id: Long) {}
}