package com.angkringan.tugas10.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.angkringan.tugas10.data.Note

@Composable
fun NotesScreen(uiState: NotesUiState) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (uiState) {
            is NotesUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
            }
            is NotesUiState.Error -> {
                Text(text = uiState.message, modifier = Modifier.testTag("error_text"))
            }
            is NotesUiState.Success -> {
                if (uiState.notes.isEmpty()) {
                    Text(text = "Belum ada catatan", modifier = Modifier.testTag("empty_text"))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize().testTag("notes_list")) {
                        items(uiState.notes) { note ->
                            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                                    Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}