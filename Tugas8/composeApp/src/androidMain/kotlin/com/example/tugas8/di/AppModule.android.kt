package com.example.tugas8.di

import com.example.tugas8.DeviceInfo
import org.koin.dsl.module

actual val platformModule = module {
    single { DeviceInfo() }
}
