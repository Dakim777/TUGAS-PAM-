package com.example.tugas8.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val commonModule = module {
    // Tambahkan dependency umum di sini
}

fun appModule() = listOf(commonModule, platformModule)
