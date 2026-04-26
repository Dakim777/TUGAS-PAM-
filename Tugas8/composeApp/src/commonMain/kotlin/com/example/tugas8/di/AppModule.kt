package com.example.tugas8.di

import com.example.tugas8.NotesRepository
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val commonModule = module {
    single { NotesRepository() }
}

fun appModule() = listOf(commonModule, platformModule)
