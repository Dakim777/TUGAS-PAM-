package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.myapplication.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Aktifkan EdgeToEdge
        enableEdgeToEdge()

        setContent {
            val view = LocalView.current
            
            App(
                onThemeChanged = { isDarkMode ->
                    // Mengatur warna status bar dan ikon secara dinamis
                    val window = this.window
                    window.statusBarColor = Color.Transparent.toArgb()
                    window.navigationBarColor = Color.Transparent.toArgb()
                    
                    val controller = WindowCompat.getInsetsController(window, view)
                    // Jika dark mode OFF, ikon status bar harus gelap (karena bg putih)
                    // Jika dark mode ON, ikon status bar harus putih
                    controller.isAppearanceLightStatusBars = !isDarkMode
                    controller.isAppearanceLightNavigationBars = !isDarkMode
                }
            )
        }
    }
}