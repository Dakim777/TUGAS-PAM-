package com.example.tugas7

import androidx.compose.ui.window.ComposeUIViewController
import com.example.notes.db.DatabaseDriverFactory

fun MainViewController() = ComposeUIViewController { 
    // Inisialisasi driver factory untuk iOS
    val driverFactory = DatabaseDriverFactory()
    
    // Memanggil App dengan driverFactory
    App(driverFactory) 
}
