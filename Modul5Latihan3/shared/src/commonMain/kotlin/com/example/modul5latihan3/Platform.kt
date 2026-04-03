package com.example.modul5latihan3

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform