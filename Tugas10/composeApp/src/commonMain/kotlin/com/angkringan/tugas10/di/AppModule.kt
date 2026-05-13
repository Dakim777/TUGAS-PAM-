package com.angkringan.tugas10.di

import com.angkringan.tugas10.data.NoteDatabase
import com.angkringan.tugas10.data.NoteRepository
import com.angkringan.tugas10.data.NoteRepositoryImpl
import com.angkringan.tugas10.data.NoteValidator
import com.angkringan.tugas10.presentation.NotesViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { NoteDatabase() }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    factory { NoteValidator() }
}

val viewModelModule = module {
    viewModel { NotesViewModel(get()) }
}

val allModules = listOf(dataModule, viewModelModule)

fun initKoin() {
    startKoin {
        modules(allModules)
    }
}