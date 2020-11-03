package com.fingerprintjs.android.fingerprint.fingerprinters.hardware

import com.fingerprintjs.android.fingerprint.datasources.InputDeviceData
import com.fingerprintjs.android.fingerprint.datasources.SensorData

data class HardwareFingerprintRawData(
    val manufacturerName: String,
    val modelName: String,
    val totalRAM: Long,
    val totalInternalStorageSpace: Long,
    val procCpuInfo: Map<String, String>,
    val sensors: List<SensorData>,
    val inputDevices: List<InputDeviceData>
)