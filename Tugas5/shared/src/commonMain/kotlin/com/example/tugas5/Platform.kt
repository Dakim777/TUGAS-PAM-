package com.example.tugas5

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform