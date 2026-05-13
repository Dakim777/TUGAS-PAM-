package com.angkringan.tugas10.presentation

import app.cash.turbine.test
import com.angkringan.tugas10.data.Note
import com.angkringan.tugas10.data.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

class NotesViewModelTest {
    private lateinit var mockRepo: NoteRepository
    private lateinit var viewModel: NotesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepo = mockk()
        val dummyNotes = listOf(Note(1, "Test", "Content"))
        coEvery { mockRepo.getAllNotes() } returns flowOf(dummyNotes)
        viewModel = NotesViewModel(mockRepo)
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

    // Kasus 2 (MockK Verification)
    @Test
    fun test2_addNoteCallsRepository() = runTest {
        coEvery { mockRepo.insertNote(any()) } just runs
        viewModel.addNote("New", "Note")
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockRepo.insertNote(any()) }
    }

    // Kasus 3 (MockK Verification)
    @Test
    fun test3_deleteNoteCallsRepository() = runTest {
        coEvery { mockRepo.deleteNote(1L) } just runs
        viewModel.deleteNote(1L)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockRepo.deleteNote(1L) }
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