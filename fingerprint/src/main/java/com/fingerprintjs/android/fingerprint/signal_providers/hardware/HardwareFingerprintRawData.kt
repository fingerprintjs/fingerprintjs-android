package com.fingerprintjs.android.fingerprint.signal_providers.hardware


import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.SensorData
import com.fingerprintjs.android.fingerprint.signal_providers.IdentificationSignal
import com.fingerprintjs.android.fingerprint.signal_providers.RawData
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfo
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public data class HardwareFingerprintRawData(
    val manufacturerName: String,
    val modelName: String,
    val totalRAM: Long,
    val totalInternalStorageSpace: Long,
    val procCpuInfo: Map<String, String>,
    val procCpuInfoV2: CpuInfo,
    val sensors: List<SensorData>,
    val inputDevices: List<InputDeviceData>,
    val batteryHealth: String,
    val batteryFullCapacity: String,
    val cameraList: List<CameraInfo>,
    val glesVersion: String,
    val abiType: String,
    val coresCount: Int
) : RawData() {

    override fun signals(): List<IdentificationSignal<out Any>> = listOf(
        manufacturerName(),
        modelName(),
        totalRAM(),
        totalInternalStorageSpace(),
        procCpuInfo(),
        procCpuInfoV2(),
        sensors(),
        inputDevices(),
        inputDevicesV2(),
        batteryHealth(),
        batteryFullCapacity(),
        cameraList(),
        glesVersion(),
        abiType(),
        coresCount()
    )

    public fun manufacturerName(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.STABLE,
        MANUFACTURER_NAME_KEY,
        MANUFACTURER_DISPLAY_NAME,
        manufacturerName
    ) {
        override fun toString() = manufacturerName
    }

    public fun modelName(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.STABLE,
        MODEL_NAME_KEY,
        MODEL_DISPLAY_NAME,
        modelName
    ) {
        override fun toString() = modelName
    }

    public fun totalRAM(): IdentificationSignal<Long> = object : IdentificationSignal<Long>(
        1,
        null,
        StabilityLevel.STABLE,
        TOTAL_RAM_KEY,
        TOTAL_RAM_DISPLAY_NAME,
        totalRAM
    ) {
        override fun toString() = totalRAM.toString()
    }

    public fun totalInternalStorageSpace(): IdentificationSignal<Long> = object : IdentificationSignal<Long>(
        1,
        null,
        StabilityLevel.STABLE,
        TOTAL_INTERNAL_STORAGE_SPACE_KEY,
        TOTAL_INTERNAL_STORAGE_SPACE_DISPLAY_NAME,
        totalInternalStorageSpace
    ) {
        override fun toString() = totalInternalStorageSpace.toString()
    }

    public fun procCpuInfo(): IdentificationSignal<Map<String, String>> = object : IdentificationSignal<Map<String, String>>(
        1,
        4,
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

    public fun procCpuInfoV2(): IdentificationSignal<CpuInfo> = object : IdentificationSignal<CpuInfo>(
        4,
        null,
        StabilityLevel.STABLE,
        CPU_INFO_KEY,
        CPU_INFO_DISPLAY_NAME,
        procCpuInfoV2.copy(
            commonInfo = procCpuInfoV2.commonInfo
                .filter { it.first.lowercase() !in CPUINFO_IGNORED_COMMON_PROPS },
            perProcessorInfo = procCpuInfoV2.perProcessorInfo
                .map { procInfo -> procInfo.filter { it.first.lowercase() !in CPUINFO_IGNORED_PER_PROC_PROPS } },
        ),
    ) {
        override fun toString(): String {
            return value.commonInfo.toString() + value.perProcessorInfo.toString()
        }
    }

    public fun sensors(): IdentificationSignal<List<SensorData>> = object : IdentificationSignal<List<SensorData>>(
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

    public fun inputDevices(): IdentificationSignal<List<InputDeviceData>> = object : IdentificationSignal<List<InputDeviceData>>(
        1,
        4,
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

    // same as inputDevices(), but sorted
    public fun inputDevicesV2(): IdentificationSignal<List<InputDeviceData>> = object : IdentificationSignal<List<InputDeviceData>>(
        4,
        null,
        StabilityLevel.STABLE,
        INPUT_DEVICES_KEY,
        INPUT_DEVICES_DISPLAY_NAME,
        inputDevices
    ) {
        override fun toString(): String {
            val sb = StringBuilder()
            value
                .map { "${it.name}${it.vendor}" }
                .sortedBy { it }
                .forEach { sb.append(it) }
            return sb.toString()
        }
    }

    public fun batteryHealth(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        BATTERY_HEALTH_KEY,
        BATTERY_HEALTH_DISPLAY_NAME,
        batteryHealth
    ) {
        override fun toString() = batteryHealth
    }

    public fun batteryFullCapacity(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.STABLE,
        BATTERY_FULL_CAPACITY_KEY,
        BATTERY_FULL_CAPACITY_DISPLAY_NAME,
        batteryFullCapacity
    ) {
        override fun toString() = batteryFullCapacity
    }

    public fun cameraList(): IdentificationSignal<List<CameraInfo>> = object : IdentificationSignal<List<CameraInfo>>(
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

    public fun glesVersion(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.STABLE,
        GLES_VERSION_KEY,
        GLES_VERSION_DISPLAY_NAME,
        glesVersion
    ) {
        override fun toString() = glesVersion
    }

    public fun abiType(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.STABLE,
        ABI_TYPE_KEY,
        ABI_TYPE_DISPLAY_NAME,
        abiType
    ) {
        override fun toString() = abiType
    }

    public fun coresCount(): IdentificationSignal<Int> = object : IdentificationSignal<Int>(
        2,
        null,
        StabilityLevel.STABLE,
        CORES_COUNT_KEY,
        CORES_COUNT_DISPLAY_NAME,
        coresCount
    ) {
        override fun toString() = coresCount.toString()
    }

    private companion object {
        private val CPUINFO_IGNORED_COMMON_PROPS = setOf(
            "processor",
        )

        private val CPUINFO_IGNORED_PER_PROC_PROPS = setOf(
            "bogomips",
            "cpu mhz",
        )
    }
}