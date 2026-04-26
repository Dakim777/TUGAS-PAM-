package com.example.tugas8

import android.content.Context
import android.os.BatteryManager

actual class BatteryInfo(private val context: Context) {
    actual fun getBatteryLevel(): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}
