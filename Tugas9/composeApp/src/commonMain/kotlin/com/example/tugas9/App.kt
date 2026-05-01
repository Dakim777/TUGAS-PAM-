package com.example.tugas9

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

// Palet Warna Angkringan Modern
private val AngkringanBrown = Color(0xFF3E2723)
private val AngkringanBrownLight = Color(0xFF5D4037)
private val AngkringanAmber = Color(0xFFFFB300)
private val AngkringanCream = Color(0xFFFFF8E1)
private val AngkringanSurface = Color(0xFFFAF9F6)

private val LightColorScheme = lightColorScheme(
    primary = AngkringanBrown,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD7CCC8),
    onPrimaryContainer = AngkringanBrown,
    secondary = AngkringanAmber,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFECB3),
    onSecondaryContainer = Color(0xFF5D4037),
    surface = AngkringanSurface,
    onSurface = AngkringanBrown,
    background = Color.White
)

@Composable
fun App(viewModel: ChatViewModel) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography()
    ) {
        ChatScreen(viewModel = viewModel)
    }
}
