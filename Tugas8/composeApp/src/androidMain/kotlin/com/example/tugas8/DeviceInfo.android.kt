package com.example.tugas8

import android.os.Build

actual class DeviceInfo actual constructor() {
    actual fun getDeviceName(): String = Build.MODEL
    actual fun getOsVersion(): String = "Android ${Build.VERSION.RELEASE}"
}
