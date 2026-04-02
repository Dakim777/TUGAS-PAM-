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

// Palette Biru - Putih - Hitam (Kotlin Style Blue)
private val KotlinBlue = Color(0xFF0095D5)
private val DeepBlue = Color(0xFF0077AA)
private val DarkBackground = Color(0xFF0F172A) // Hitam kebiruan modern
private val DarkSurface = Color(0xFF1E293B)

private val LightColors = lightColors(
    primary = KotlinBlue,
    primaryVariant = DeepBlue,
    secondary = KotlinBlue,
    background = Color(0xFFF1F5F9), // Putih kebiruan sangat terang
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color(0xFF1E293B),
    onSurface = Color(0xFF1E293B)
)

private val DarkColors = darkColors(
    primary = Color(0xFF38BDF8), // Biru cerah untuk dark mode
    primaryVariant = KotlinBlue,
    secondary = Color(0xFF38BDF8),
    background = DarkBackground,
    surface = DarkSurface,
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