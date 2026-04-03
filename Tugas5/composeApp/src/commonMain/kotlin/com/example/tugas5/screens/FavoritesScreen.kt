package com.example.tugas5.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tugas5.components.NoteItem
import com.example.tugas5.data.NoteRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    repository: NoteRepository,
    onNoteClick: (String) -> Unit
) {
    val notes by repository.notes.collectAsState()
    val favoriteNotes = notes.filter { it.isFavorite }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (favoriteNotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No favorite notes yet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteNotes) { note ->
                    NoteItem(
                        note = note,
                        onClick = { onNoteClick(note.id) },
                        onFavoriteClick = { repository.toggleFavorite(note.id) }
                    )
                }
            }
        }
    }
}
