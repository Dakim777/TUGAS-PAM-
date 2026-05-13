package com.angkringan.tugas10.presentation

import app.cash.turbine.test
import com.angkringan.tugas10.data.Note
import com.angkringan.tugas10.data.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

// --- FAKE REPOSITORY SEBAGAI PENGGANTI MOCKK ---
class FakeNoteRepository : NoteRepository {
    var isInsertCalled = false
    var isDeleteCalled = false
    private val dummyNotes = listOf(Note(1, "Test", "Content"))

    override fun getAllNotes(): Flow<List<Note>> {
        return flowOf(dummyNotes)
    }

    override suspend fun insertNote(note: Note) {
        isInsertCalled = true
    }

    override suspend fun deleteNote(id: Long) {
        isDeleteCalled = true
    }
}

class NotesViewModelTest {
    private lateinit var fakeRepo: FakeNoteRepository
    private lateinit var viewModel: NotesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeNoteRepository() // Pakai Fake
        viewModel = NotesViewModel(fakeRepo)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Kasus 1 (Turbine Flow Test 1)
    @Test
    fun test1_initialStateEmitsSuccess() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertIs<NotesUiState.Success>(state)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Kasus 2 (Pengganti coVerify MockK)
    @Test
    fun test2_addNoteCallsRepository() = runTest {
        viewModel.addNote("New", "Note")
        testDispatcher.scheduler.advanceUntilIdle()
        // Memastikan fungsi insert di repository beneran dipanggil
        assertTrue(fakeRepo.isInsertCalled)
    }

    // Kasus 3 (Pengganti coVerify MockK)
    @Test
    fun test3_deleteNoteCallsRepository() = runTest {
        viewModel.deleteNote(1L)
        testDispatcher.scheduler.advanceUntilIdle()
        // Memastikan fungsi delete di repository beneran dipanggil
        assertTrue(fakeRepo.isDeleteCalled)
    }

    // Kasus 4 (Turbine Flow Test 2)
    @Test
    fun test4_uiStateFlowCapturesState() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertIs<NotesUiState.Success>(state)
            cancelAndIgnoreRemainingEvents()
        }
    }
}