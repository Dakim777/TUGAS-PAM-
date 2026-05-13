package com.angkringan.tugas10

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform