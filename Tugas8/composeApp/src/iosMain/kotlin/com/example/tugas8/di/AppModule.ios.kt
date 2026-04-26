package com.example.tugas8.di

import com.example.tugas8.DeviceInfo
import com.example.tugas8.NetworkMonitor
import org.koin.dsl.module

actual val platformModule = module {
    single { DeviceInfo() }
    single { NetworkMonitor() }
}
