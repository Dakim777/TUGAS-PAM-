package com.angkringan.tugas10.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.angkringan.tugas10.data.Note
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    // Rule wajib untuk menjalankan testing UI di Compose
    @get:Rule
    val composeTestRule = createComposeRule()

    // Kasus 1: Memastikan indikator loading muncul saat state Loading
    @Test
    fun test1_loadingState_displaysIndicator() {
        composeTestRule.setContent {
            NotesScreen(uiState = NotesUiState.Loading)
        }

        // Mengecek elemen berdasarkan Test Tag
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    // Kasus 2: Memastikan teks kosong muncul saat state Success tapi data kosong
    @Test
    fun test2_emptyState_displaysEmptyMessage() {
        composeTestRule.setContent {
            NotesScreen(uiState = NotesUiState.Success(emptyList()))
        }

        composeTestRule.onNodeWithTag("empty_text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Belum ada catatan").assertIsDisplayed()
    }

    // Kasus 3: Memastikan List dan Item catatan muncul saat state Success ada datanya
    @Test
    fun test3_successState_displaysNotesList() {
        val dummyNotes = listOf(
            Note(1, "Persiapan Sertifikasi", "Belajar Domain 1 CISSP"),
            Note(2, "Progress Skripsi", "Mengerjakan Bab 2")
        )

        composeTestRule.setContent {
            NotesScreen(uiState = NotesUiState.Success(dummyNotes))
        }

        composeTestRule.onNodeWithTag("notes_list").assertIsDisplayed()
        // Mengecek elemen berdasarkan teks judul yang ada di dalamnya
        composeTestRule.onNodeWithText("Persiapan Sertifikasi").assertIsDisplayed()
        composeTestRule.onNodeWithText("Progress Skripsi").assertIsDisplayed()
    }
}