package com.example.tugas8

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class NetworkMonitor {
    actual fun observeConnectivity(): Flow<Boolean> = flowOf(true)
}
