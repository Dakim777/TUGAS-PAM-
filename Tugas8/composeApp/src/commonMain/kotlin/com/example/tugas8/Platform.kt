package com.example.tugas8

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform