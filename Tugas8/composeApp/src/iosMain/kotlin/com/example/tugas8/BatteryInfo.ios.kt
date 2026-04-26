package com.example.tugas8

import platform.UIKit.UIDevice

actual class BatteryInfo {
    actual fun getBatteryLevel(): Int {
        UIDevice.currentDevice.batteryMonitoringEnabled = true
        val level = UIDevice.currentDevice.batteryLevel
        return if (level < 0f) 0 else (level * 100).toInt()
    }
}
