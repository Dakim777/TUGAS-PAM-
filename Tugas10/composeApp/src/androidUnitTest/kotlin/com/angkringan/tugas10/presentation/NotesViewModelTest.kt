package com.angkringan.tugas10.presentation

import app.cash.turbine.test
import com.angkringan.tugas10.data.Note
import com.angkringan.tugas10.data.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FakeNoteRepository : NoteRepository {
    var isInsertCalled = false
    var isDeleteCalled = false
    var insertedNote: Note? = null
    var deletedId: Long? = null

    private val _notesFlow = MutableStateFlow<List<Note>>(emptyList())

    fun setNotes(notes: List<Note>) {
        _notesFlow.value = notes
    }

    override fun getAllNotes(): Flow<List<Note>> = _notesFlow

    override suspend fun insertNote(note: Note) {
        isInsertCalled = true
        insertedNote = note
        _notesFlow.value = _notesFlow.value + note
    }

    override suspend fun deleteNote(id: Long) {
        isDeleteCalled = true
        deletedId = id
        _notesFlow.value = _notesFlow.value.filter { it.id != id }
    }
}

class NotesViewModelTest {
    private lateinit var fakeRepo: FakeNoteRepository
    private lateinit var viewModel: NotesViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeNoteRepository()
        fakeRepo.setNotes(listOf(Note(1, "Test", "Content")))
        viewModel = NotesViewModel(fakeRepo, testScope)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Flow Test 1 — state awal Success
    @Test
    fun test1_initialStateEmitsSuccess() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.uiState.test {
            assertIs<NotesUiState.Success>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 2 — addNote memanggil repository
    @Test
    fun test2_addNoteCallsRepository() = runTest {
        viewModel.addNote("New", "Note")
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(fakeRepo.isInsertCalled)
    }

    // Test 3 — deleteNote memanggil repository
    @Test
    fun test3_deleteNoteCallsRepository() = runTest {
        viewModel.deleteNote(1L)
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(fakeRepo.isDeleteCalled)
    }

    // Flow Test 2 — state berisi notes
    @Test
    fun test4_uiStateContainsNotes() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            assertIs<NotesUiState.Success>(state)
            assertTrue(state.notes.isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 5 — addNote menyimpan title yang benar
    @Test
    fun test5_addNoteStoresCorrectTitle() = runTest {
        viewModel.addNote("Belanja", "Susu")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Belanja", fakeRepo.insertedNote?.title)
    }

    // Test 6 — addNote menyimpan content yang benar
    @Test
    fun test6_addNoteStoresCorrectContent() = runTest {
        viewModel.addNote("Judul", "Isi Catatan")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Isi Catatan", fakeRepo.insertedNote?.content)
    }

    // Test 7 — deleteNote menyimpan id yang benar
    @Test
    fun test7_deleteNotePassesCorrectId() = runTest {
        viewModel.deleteNote(5L)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(5L, fakeRepo.deletedId)
    }

    // Flow Test 3 — setelah addNote, state terupdate
    @Test
    fun test8_afterAddNoteStateUpdates() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.uiState.test {
            // State awal
            val initial = awaitItem()
            assertIs<NotesUiState.Success>(initial)
            val initialSize = initial.notes.size

            // Tambah note
            viewModel.addNote("Baru", "Isi")
            testDispatcher.scheduler.advanceUntilIdle()

            // State setelah tambah
            val updated = awaitItem()
            assertIs<NotesUiState.Success>(updated)
            assertEquals(initialSize + 1, updated.notes.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // Flow Test 4 — setelah deleteNote, state terupdate
    @Test
    fun test9_afterDeleteNoteStateUpdates() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.uiState.test {
            // State awal ada 1 note
            val initial = awaitItem()
            assertIs<NotesUiState.Success>(initial)
            assertEquals(1, initial.notes.size)

            // Hapus note
            viewModel.deleteNote(1L)
            testDispatcher.scheduler.advanceUntilIdle()

            // State setelah hapus kosong
            val updated = awaitItem()
            assertIs<NotesUiState.Success>(updated)
            assertTrue(updated.notes.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 10 — initial state loading sebelum collect
    @Test
    fun test10_initialStateIsLoading() = runTest {
        // Buat viewModel baru tanpa advance dispatcher
        val freshRepo = FakeNoteRepository()
        val freshVm = NotesViewModel(freshRepo, testScope)
        assertIs<NotesUiState.Loading>(freshVm.uiState.value)
    }
}