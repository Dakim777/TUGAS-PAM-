package com.angkringan.tugas10.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NoteRepositoryTest {
    private lateinit var repository: NoteRepository
    private lateinit var database: NoteDatabase

    @BeforeTest
    fun setup() {
        database = NoteDatabase()
        repository = NoteRepositoryImpl(database)
    }

    @Test
    fun test1_initialStateIsEmpty() = runTest {
        val notes = repository.getAllNotes().first()
        assertTrue(notes.isEmpty())
    }

    @Test
    fun test2_insertNoteAddsToList() = runTest {
        repository.insertNote(Note(1, "Judul", "Isi"))
        val notes = repository.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals("Judul", notes[0].title)
    }

    @Test
    fun test3_deleteNoteRemovesFromList() = runTest {
        repository.insertNote(Note(1, "A", "B"))
        repository.deleteNote(1L)
        val notes = repository.getAllNotes().first()
        assertTrue(notes.isEmpty())
    }

    @Test
    fun test4_deleteNonExistentNoteDoesNothing() = runTest {
        repository.insertNote(Note(1, "A", "B"))
        repository.deleteNote(99L)
        val notes = repository.getAllNotes().first()
        assertEquals(1, notes.size)
    }

    @Test
    fun test5_insertMultipleNotes() = runTest {
        repository.insertNote(Note(1, "A", "A"))
        repository.insertNote(Note(2, "B", "B"))
        val notes = repository.getAllNotes().first()
        assertEquals(2, notes.size)
    }
}