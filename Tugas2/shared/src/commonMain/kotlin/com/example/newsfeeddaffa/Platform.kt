package com.example.newsfeeddaffa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform