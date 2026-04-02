package com.example.myapplication

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.ProfileScreen
import com.example.myapplication.viewmodel.ProfileViewModel

// Custom Palette untuk tampilan lebih modern
private val PrimaryColor = Color(0xFF6200EE)
private val PrimaryVariant = Color(0xFF3700B3)
private val SecondaryColor = Color(0xFF03DAC6)

private val LightColors = lightColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryVariant,
    secondary = SecondaryColor,
    background = Color(0xFFF8F9FA),
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color(0xFF212121),
    onSurface = Color(0xFF212121)
)

private val DarkColors = darkColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = PrimaryVariant,
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun App(
    profileViewModel: ProfileViewModel = viewModel(),
    onThemeChanged: (Boolean) -> Unit = {}
) {
    val uiState by profileViewModel.uiState.collectAsState()

    // Memberitahu platform (Android) saat tema berubah
    LaunchedEffect(uiState.isDarkMode) {
        onThemeChanged(uiState.isDarkMode)
    }

    val colors = if (uiState.isDarkMode) DarkColors else LightColors

    MaterialTheme(colors = colors) {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            ProfileScreen(profileViewModel)
        }
    }
}