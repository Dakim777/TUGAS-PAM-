package com.example.tugas9

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform