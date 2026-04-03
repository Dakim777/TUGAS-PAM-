package com.example.latihanmodul5

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Latihanmodul5",
    ) {
        App()
    }
}