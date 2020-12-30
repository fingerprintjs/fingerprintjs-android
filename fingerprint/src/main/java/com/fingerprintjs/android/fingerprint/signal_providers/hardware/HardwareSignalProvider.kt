package com.fingerprintjs.android.fingerprint.signal_providers.hardware


import com.fingerprintjs.android.fingerprint.datasources.BatteryInfoDataSource
import com.fingerprintjs.android.fingerprint.datasources.CameraInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.CodecInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.datasources.MemInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.SensorDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.SignalProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class HardwareSignalProvider(
        cpuInfoProvider: CpuInfoProvider,
        memInfoProvider: MemInfoProvider,
        osBuildInfoProvider: OsBuildInfoProvider,
        sensorsDataSource: SensorDataSource,
        inputDeviceDataSource: InputDeviceDataSource,
        batteryInfoDataSource: BatteryInfoDataSource,
        cameraInfoProvider: CameraInfoProvider,
        private val hasher: Hasher,
        version: Int
) : SignalProvider<HardwareFingerprintRawData>(
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
                    cameraInfoProvider.getCameraInfo()
            )

    override fun fingerprint(): String {
        return hasher.hash(when (version) {
            1 -> v1()
            2 -> v2()
            else -> v2()
        })
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

        sb.append(rawData.batteryFullCapacity)
        sb.append(rawData.batteryHealth)

        rawData.cameraList.forEach {
            sb.append(it.cameraName).append(it.cameraType).append(it.cameraOrientation)
        }

        return sb.toString()
    }

    override fun rawData() = rawData
}