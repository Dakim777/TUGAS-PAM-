package com.example.tugas8

import platform.UIKit.UIDevice

actual class DeviceInfo actual constructor() {
    actual fun getDeviceName(): String = UIDevice.currentDevice.name
    actual fun getOsVersion(): String = UIDevice.currentDevice.systemVersion
}
