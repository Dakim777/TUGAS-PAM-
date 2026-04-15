package com.example.notes.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class NoteRepository(database: NotesDatabase) {
    private val queries = database.noteQueries

    fun getAllNotes(): Flow<List<Note>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        return queries.search(query, query)
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    suspend fun insertNote(title: String, content: String) {
        withContext(Dispatchers.Default) {
            val now = Clock.System.now().toEpochMilliseconds()
            queries.insert(
                title = title,
                content = content,
                created_at = now,
                updated_at = now
            )
        }
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        withContext(Dispatchers.Default) {
            val now = Clock.System.now().toEpochMilliseconds()
            queries.update(
                title = title,
                content = content,
                updated_at = now,
                id = id
            )
        }
    }

    suspend fun deleteNote(id: Long) {
        withContext(Dispatchers.Default) {
            queries.delete(id)
        }
    }
}
