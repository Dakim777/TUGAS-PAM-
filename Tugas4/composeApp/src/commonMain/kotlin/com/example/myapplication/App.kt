package com.example.myapplication

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.ProfileScreen
import com.example.myapplication.viewmodel.ProfileViewModel

@Composable
fun App(profileViewModel: ProfileViewModel = viewModel()) {
    val uiState by profileViewModel.uiState.collectAsState()

    // Menentukan tema warna secara dinamis [cite: 572]
    val colors = if (uiState.isDarkMode) darkColors() else lightColors()

    MaterialTheme(colors = colors) {
        ProfileScreen(profileViewModel)
    }
}