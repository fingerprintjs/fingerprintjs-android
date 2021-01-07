package com.fingerprintjs.android.fingerprint.signal_providers.hardware


import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo
import com.fingerprintjs.android.fingerprint.info_providers.SensorData


data class HardwareFingerprintRawData(
        val manufacturerName: String,
        val modelName: String,
        val totalRAM: Long,
        val totalInternalStorageSpace: Long,
        val procCpuInfo: Map<String, String>,
        val sensors: List<SensorData>,
        val inputDevices: List<InputDeviceData>,
        val batteryHealth: String,
        val batteryFullCapacity: String,
        val cameraList: List<CameraInfo>,
        val glesVersion: String,
        val abiType: String,
        val coresCount: Int
)