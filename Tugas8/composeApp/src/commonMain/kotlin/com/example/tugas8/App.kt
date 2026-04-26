package com.example.tugas8

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

// Matcha Color Palette
val MatchaPrimary = Color(0xFF8BA888)
val MatchaSecondary = Color(0xFFC1D4C0)
val MatchaBackground = Color(0xFFF1F8F1)
val MatchaSurface = Color(0xFFFFFFFF)
val MatchaOnSurface = Color(0xFF2E3D2E)

@Composable
fun App() {
    KoinAppContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinAppContent() {
    var currentScreen by remember { mutableStateOf(Screen.Main) }

    val matchaColorScheme = lightColorScheme(
        primary = MatchaPrimary,
        secondary = MatchaSecondary,
        background = MatchaBackground,
        surface = MatchaSurface,
        onSurface = MatchaOnSurface,
        onPrimary = Color.White
    )

    MaterialTheme(colorScheme = matchaColorScheme) {
        Scaffold(
            topBar = {
                Column {
                    NetworkStatusIndicator()
                    CenterAlignedTopAppBar(
                        title = { Text(if (currentScreen == Screen.Main) "Matcha Notes" else "Settings") },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MatchaPrimary,
                            titleContentColor = Color.White
                        )
                    )
                }
            },
            bottomBar = {
                NavigationBar(containerColor = MatchaSecondary) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.Main,
                        onClick = { currentScreen = Screen.Main },
                        icon = { Icon(Icons.Default.List, contentDescription = "Notes") },
                        label = { Text("Notes") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Settings,
                        onClick = { currentScreen = Screen.Settings },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") }
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize().background(MatchaBackground)) {
                when (currentScreen) {
                    Screen.Main -> MainScreen()
                    Screen.Settings -> SettingsScreen()
                }
            }
        }
    }
}

enum class Screen {
    Main, Settings
}

@Composable
fun NetworkStatusIndicator(networkMonitor: NetworkMonitor = koinInject()) {
    val isConnected by networkMonitor.observeConnectivity().collectAsState(initial = true)

    AnimatedVisibility(
        visible = !isConnected,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE57373)) // Muted Red for warning
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Internet Connection",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MainScreen(repository: NotesRepository = koinInject()) {
    val notes = repository.notes
    var showDialog by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    noteToEdit = null
                    title = ""
                    content = ""
                    showDialog = true
                },
                containerColor = MatchaPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        containerColor = Color.Transparent
    ) {
        if (notes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada catatan matcha...", color = MatchaOnSurface.copy(alpha = 0.6f))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteItem(
                        note = note,
                        onEdit = {
                            noteToEdit = note
                            title = note.title
                            content = note.content
                            showDialog = true
                        },
                        onDelete = {
                            repository.deleteNote(note.id)
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (noteToEdit == null) "Tambah Catatan" else "Edit Catatan") },
            text = {
                Column {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Judul") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Isi Catatan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteToEdit == null) {
                            repository.addNote(title, content)
                        } else {
                            repository.updateNote(noteToEdit!!.id, title, content)
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MatchaPrimary)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun NoteItem(note: Note, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MatchaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.0f)) {
                Text(note.title, style = MaterialTheme.typography.titleMedium, color = MatchaOnSurface)
                Text(note.content, style = MaterialTheme.typography.bodySmall, color = MatchaOnSurface.copy(alpha = 0.7f))
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MatchaPrimary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE57373))
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    val deviceInfo: DeviceInfo = koinInject()
    val batteryInfo: BatteryInfo = koinInject()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Device Information", style = MaterialTheme.typography.headlineSmall, color = MatchaOnSurface)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MatchaSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow(label = "Model", value = deviceInfo.getDeviceName())
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MatchaSecondary)
                InfoRow(label = "OS Version", value = deviceInfo.getOsVersion())
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MatchaSecondary)
                InfoRow(label = "Battery Level", value = "${batteryInfo.getBatteryLevel()}%")
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MatchaOnSurface.copy(alpha = 0.6f))
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = MatchaOnSurface)
    }
}
