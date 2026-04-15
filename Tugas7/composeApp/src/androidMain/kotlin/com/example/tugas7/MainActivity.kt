package com.example.tugas7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notes.db.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Inisialisasi driver factory dengan context Android
        val driverFactory = DatabaseDriverFactory(this)

        setContent {
            // Memanggil App dengan driverFactory agar database bisa jalan
            App(driverFactory)
        }
    }
}
