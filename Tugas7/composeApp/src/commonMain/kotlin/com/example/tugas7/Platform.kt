package com.example.tugas7

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun SystemAppearance(isDark: Boolean)
