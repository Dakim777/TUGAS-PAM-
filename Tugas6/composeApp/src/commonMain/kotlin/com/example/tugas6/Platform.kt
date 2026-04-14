package com.example.tugas6

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform