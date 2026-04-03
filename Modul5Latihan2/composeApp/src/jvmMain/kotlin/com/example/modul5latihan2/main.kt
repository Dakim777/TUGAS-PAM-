package com.example.modul5latihan2

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Modul5Latihan2",
    ) {
        App()
    }
}