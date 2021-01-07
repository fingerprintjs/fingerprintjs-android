package com.fingerprintjs.android.fingerprint.signal_providers.hardware


import com.fingerprintjs.android.fingerprint.info_providers.BatteryInfoDataSource
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.GpuInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.info_providers.MemInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.SensorDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class HardwareSignalGroupProvider(
    cpuInfoProvider: CpuInfoProvider,
    memInfoProvider: MemInfoProvider,
    osBuildInfoProvider: OsBuildInfoProvider,
    sensorsDataSource: SensorDataSource,
    inputDeviceDataSource: InputDeviceDataSource,
    batteryInfoDataSource: BatteryInfoDataSource,
    cameraInfoProvider: CameraInfoProvider,
    gpuInfoProvider: GpuInfoProvider,
    private val hasher: Hasher,
    version: Int
) : SignalGroupProvider<HardwareFingerprintRawData>(
    version
) {
    private val rawData =
        HardwareFingerprintRawData(
            osBuildInfoProvider.manufacturerName(),
            osBuildInfoProvider.modelName(),
            memInfoProvider.totalRAM(),
            memInfoProvider.totalInternalStorageSpace(),
            cpuInfoProvider.cpuInfo(),
            sensorsDataSource.sensors(),
            inputDeviceDataSource.getInputDeviceData(),
            batteryInfoDataSource.batteryHealth(),
            batteryInfoDataSource.batteryTotalCapacity(),
            cameraInfoProvider.getCameraInfo(),
            gpuInfoProvider.glesVersion(),
            cpuInfoProvider.abiType(),
            cpuInfoProvider.coresCount()
        )

    override fun fingerprint(): String {
        return hasher.hash(
            when (version) {
                1 -> v1()
                2 -> v2()
                else -> v2()
            }
        )
    }

    private fun v1(): String {
        val sb = StringBuilder()
        sb.append(rawData.manufacturerName)
        sb.append(rawData.modelName)
        sb.append(rawData.totalRAM)
        sb.append(rawData.totalInternalStorageSpace)
        rawData.procCpuInfo.entries.forEach {
            sb.append(it.key).append(it.value)
        }

        rawData.sensors.forEach {
            sb.append(it.sensorName).append(it.vendorName)
        }

        rawData.inputDevices.forEach {
            sb.append(it.name).append(it.vendor)
        }

        return sb.toString()
    }

    private fun v2(): String {
        val sb = StringBuilder()
        sb.append(v1())

        sb
            .append(rawData.batteryFullCapacity)
            .append(rawData.batteryHealth)
            .append(rawData.glesVersion)
            .append(rawData.abiType)
            .append(rawData.coresCount)

        rawData.cameraList.forEach {
            sb
                .append(it.cameraName)
                .append(it.cameraType)
                .append(it.cameraOrientation)
        }

        return sb.toString()
    }

    override fun rawData() = rawData
}