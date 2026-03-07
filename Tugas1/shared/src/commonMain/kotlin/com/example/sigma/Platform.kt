package com.example.sigma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform