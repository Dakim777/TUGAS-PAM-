package com.example.modul5latihan2

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform