package com.example.myapplication

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.myapplication.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyApplication",
    ) {
        App()
    }
}