package com.example.tugas7

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.db.DatabaseDriverFactory
import com.example.notes.db.Note
import com.example.notes.db.NoteRepository
import com.example.notes.db.NotesDatabase
import com.example.notes.settings.SettingsManager
import com.example.notes.ui.*
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class Screen {
    List, Add, Edit, Settings
}

private val Green800 = Color(0xFF2E7D32)
private val Green600 = Color(0xFF43A047)
private val Green400 = Color(0xFF66BB6A)
private val Green200 = Color(0xFFA5D6A7)
private val Green100 = Color(0xFFC8E6C9)
private val Green50 = Color(0xFFE8F5E9)
private val Green900 = Color(0xFF1B5E20)
private val DarkGreenBackground = Color(0xFF0C1D0E)

private val LightColorScheme = lightColorScheme(
    primary = Green800,
    onPrimary = Color.White,
    primaryContainer = Green100,
    onPrimaryContainer = Green900,
    secondary = Green600,
    onSecondary = Color.White,
    secondaryContainer = Green50,
    onSecondaryContainer = Green900,
    tertiary = Green400,
    onTertiary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Green50,
    onSurfaceVariant = Green900,
    outline = Green800,
    outlineVariant = Green200
)

private val DarkColorScheme = darkColorScheme(
    primary = Green400,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1B301E),
    onPrimaryContainer = Green100,
    secondary = Green600,
    onSecondary = Color.White,
    secondaryContainer = Green900,
    onSecondaryContainer = Green50,
    tertiary = Green800,
    onTertiary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = DarkGreenBackground,
    onSurfaceVariant = Green50,
    outline = Green400,
    outlineVariant = Green900
)

@OptIn(ExperimentalSettingsApi::class)
@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    val scope = rememberCoroutineScope()
    
    val database = remember { NotesDatabase(driverFactory.createDriver()) }
    val repository = remember { NoteRepository(database) }
    
    val settingsManager = remember { 
        val settings = Settings()
        val observableSettings = settings as ObservableSettings
        val flowSettings = observableSettings.toFlowSettings(Dispatchers.Default)
        SettingsManager(flowSettings, scope) 
    }
    
    val viewModel: NotesViewModel = viewModel { 
        NotesViewModel(repository, settingsManager) 
    }

    val theme by settingsManager.theme.collectAsState()
    val darkTheme = when (theme) {
        SettingsManager.THEME_LIGHT -> false
        SettingsManager.THEME_DARK -> true
        else -> isSystemInDarkTheme()
    }

    SystemAppearance(darkTheme)

    var currentScreen by remember { mutableStateOf(Screen.List) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    val searchQuery by viewModel.searchQuery.collectAsState()

    MaterialTheme(colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme) {
        Surface {
            val uiState by viewModel.uiState.collectAsState()
            val sortOrder by settingsManager.sortOrder.collectAsState()

            when (currentScreen) {
                Screen.List -> {
                    NotesListScreen(
                        state = uiState,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                        onAddNoteClick = { currentScreen = Screen.Add },
                        onNoteClick = { 
                            selectedNote = it
                            currentScreen = Screen.Edit 
                        },
                        onDeleteNote = { viewModel.deleteNote(it) },
                        onSettingsClick = { currentScreen = Screen.Settings }
                    )
                }
                Screen.Add -> {
                    AddEditNoteScreen(
                        onSave = { title, content ->
                            scope.launch {
                                repository.insertNote(title, content)
                                currentScreen = Screen.List
                            }
                        },
                        onBack = { currentScreen = Screen.List }
                    )
                }
                Screen.Edit -> {
                    selectedNote?.let { note ->
                        AddEditNoteScreen(
                            initialTitle = note.title,
                            initialContent = note.content,
                            onSave = { title, content ->
                                scope.launch {
                                    repository.updateNote(note.id, title, content)
                                    currentScreen = Screen.List
                                }
                            },
                            onBack = { currentScreen = Screen.List }
                        )
                    }
                }
                Screen.Settings -> {
                    SettingsScreen(
                        currentTheme = theme,
                        currentSortOrder = sortOrder,
                        onThemeChange = { 
                            scope.launch {
                                settingsManager.updateTheme(it)
                            }
                        },
                        onSortOrderChange = { viewModel.updateSortOrder(it) },
                        onBack = { currentScreen = Screen.List }
                    )
                }
            }
        }
    }
}
