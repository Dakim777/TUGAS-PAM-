package com.example.latihanmodul5

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform