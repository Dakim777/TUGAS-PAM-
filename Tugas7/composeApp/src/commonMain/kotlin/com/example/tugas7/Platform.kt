package com.example.tugas7

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform