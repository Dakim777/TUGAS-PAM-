package com.example.tugas9

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: ChatViewModel) {
    MaterialTheme {
        ChatScreen(viewModel = viewModel)
    }
}