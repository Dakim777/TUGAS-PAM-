package com.example.myprofileapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform