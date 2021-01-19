package com.fingerprintjs.android.fingerprint.signal_providers.hardware


import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.SensorData
import com.fingerprintjs.android.fingerprint.signal_providers.RawData
import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel


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
) : RawData {

    override fun signals() = listOf(
        manufacturerName(),
        modelName(),
        totalRAM(),
        totalInternalStorageSpace(),
        procCpuInfo(),
        sensors(),
        inputDevices(),
        batteryHealth(),
        batteryFullCapacity(),
        cameraList(),
        glesVersion(),
        abiType(),
        coresCount()
    )

    fun manufacturerName() = object : Signal<String>(
        1,
        null,
        StabilityLevel.STABLE,
        MANUFACTURER_NAME_KEY,
        MANUFACTURER_DISPLAY_NAME,
        manufacturerName
    ) {
        override fun toString() = manufacturerName
    }

    fun modelName() = object : Signal<String>(
        1,
        null,
        StabilityLevel.STABLE,
        MODEL_NAME_KEY,
        MODEL_DISPLAY_NAME,
        modelName
    ) {
        override fun toString() = modelName
    }

    fun totalRAM() = object : Signal<Long>(
        1,
        null,
        StabilityLevel.STABLE,
        TOTAL_RAM_KEY,
        TOTAL_RAM_DISPLAY_NAME,
        totalRAM
    ) {
        override fun toString() = totalRAM.toString()
    }

    fun totalInternalStorageSpace() = object : Signal<Long>(
        1,
        null,
        StabilityLevel.STABLE,
        name = TOTAL_INTERNAL_STORAGE_SPACE_KEY,
        displayName = TOTAL_INTERNAL_STORAGE_SPACE_DISPLAY_NAME,
        value = totalInternalStorageSpace
    ) {
        override fun toString() = totalInternalStorageSpace.toString()
    }

    fun procCpuInfo() = object : Signal<Map<String, String>>(
        1,
        null,
        StabilityLevel.STABLE,
        CPU_INFO_KEY,
        CPU_INFO_DISPLAY_NAME,
        procCpuInfo
    ) {
        override fun toString(): String {
            val sb = StringBuilder()
            procCpuInfo.entries.forEach {
                sb.append(it.key).append(it.value)
            }
            return sb.toString()
        }
    }

    fun sensors() = object : Signal<List<SensorData>>(
        1,
        null,
        StabilityLevel.STABLE,
        SENSORS_INFO_KEY,
        SENSORS_DISPLAY_NAME,
        sensors
    ) {
        override fun toString(): String {
            val sb = StringBuilder()
            sensors.forEach {
                sb.append(it.sensorName).append(it.vendorName)
            }
            return sb.toString()
        }
    }

    fun inputDevices() = object : Signal<List<InputDeviceData>>(
        1,
        null,
        StabilityLevel.STABLE,
        INPUT_DEVICES_KEY,
        INPUT_DEVICES_DISPLAY_NAME,
        inputDevices
    ) {
        override fun toString(): String {
            val sb = StringBuilder()
            value.forEach {
                sb.append(it.name).append(it.vendor)
            }
            return sb.toString()
        }
    }

    fun batteryHealth() = object : Signal<String>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        BATTERY_HEALTH_KEY,
        BATTERY_HEALTH_DISPLAY_NAME,
        batteryHealth
    ) {
        override fun toString() = batteryHealth
    }

    fun batteryFullCapacity() = object : Signal<String>(
        2,
        null,
        StabilityLevel.STABLE,
        BATTERY_FULL_CAPACITY_KEY,
        BATTERY_FULL_CAPACITY_DISPLAY_NAME,
        batteryFullCapacity
    ) {
        override fun toString() = batteryFullCapacity
    }

    fun cameraList() = object : Signal<List<CameraInfo>>(
        2,
        null,
        StabilityLevel.STABLE,
        CAMERAS_KEY,
        CAMERAS_DISPLAY_VALUE,
        cameraList
    ) {
        override fun toString(): String {
            val sb = StringBuilder()
            cameraList.forEach {
                sb
                    .append(it.cameraName)
                    .append(it.cameraType)
                    .append(it.cameraOrientation)
            }
            return sb.toString()
        }
    }

    fun glesVersion() = object : Signal<String>(
        2,
        null,
        StabilityLevel.STABLE,
        GLES_VERSION_KEY,
        GLES_VERSION_DISPLAY_NAME,
        glesVersion
    ) {
        override fun toString() = glesVersion
    }

    fun abiType() = object : Signal<String>(
        2,
        null,
        StabilityLevel.STABLE,
        ABI_TYPE_KEY,
        ABI_TYPE_DISPLAY_NAME,
        abiType
    ) {
        override fun toString() = abiType
    }

    fun coresCount() = object : Signal<Int>(
        2,
        null,
        StabilityLevel.STABLE,
        CORES_COUNT_KEY,
        CORES_COUNT_DISPLAY_NAME,
        coresCount
    ) {
        override fun toString() = coresCount.toString()
    }
}