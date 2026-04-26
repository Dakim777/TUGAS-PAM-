package com.example.tugas8

import kotlinx.coroutines.flow.Flow

expect class NetworkMonitor {
    fun observeConnectivity(): Flow<Boolean>
}
