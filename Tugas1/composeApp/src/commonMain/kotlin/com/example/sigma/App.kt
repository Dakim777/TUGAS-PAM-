package com.example.sigma

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import sigma.composeapp.generated.resources.Res
import sigma.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    MaterialTheme { // Material Design theme
        var showContent by remember { // State management
            mutableStateOf(false)
        }

        Column( // Vertical layout
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Halo, M.Daffa Hakim Mtd!")
            Text("123140002")// Text component

            Button( // Button component
                onClick = { showContent = !showContent }
            ) {
                Text("Click me!")
            }

            AnimatedVisibility(showContent) { // Animation
                Text("Platform: ${getPlatform().name}")
            }
        }
    }
}