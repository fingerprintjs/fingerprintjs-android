package com.fingerprintjs.android.fingerprint.signal_providers.hardware


import com.fingerprintjs.android.fingerprint.info_providers.BatteryInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.GpuInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.info_providers.MemInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.SensorDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class HardwareSignalGroupProvider(
    cpuInfoProvider: CpuInfoProvider,
    memInfoProvider: MemInfoProvider,
    osBuildInfoProvider: OsBuildInfoProvider,
    sensorsDataSource: SensorDataSource,
    inputDeviceDataSource: InputDeviceDataSource,
    batteryInfoProvider: BatteryInfoProvider,
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
            batteryInfoProvider.batteryHealth(),
            batteryInfoProvider.batteryTotalCapacity(),
            cameraInfoProvider.getCameraInfo(),
            gpuInfoProvider.glesVersion(),
            cpuInfoProvider.abiType(),
            cpuInfoProvider.coresCount()
        )

    override fun fingerprint(stabilityLevel: StabilityLevel): String {
        return hasher.hash(
            when (version) {
                1 -> v1(stabilityLevel)
                2 -> v2(stabilityLevel)
                else -> v2(stabilityLevel)
            }
        )
    }

    private fun v1(stabilityLevel: StabilityLevel): String {
        val sb = StringBuilder()
        val signals = listOf(
            rawData.manufacturerName(),
            rawData.modelName(),
            rawData.totalRAM(),
            rawData.totalInternalStorageSpace(),
            rawData.procCpuInfo(),
            rawData.sensors(),
            rawData.inputDevices()
        )

        signals.forEach {
            sb.append(it.toString())
        }

        return sb.toString()
    }

    private fun v2(stabilityLevel: StabilityLevel): String {
        val sb = StringBuilder()

        // Stable
        sb.append(v1(stabilityLevel))

        sb
            .append(rawData.glesVersion)
            .append(rawData.abiType)
            .append(rawData.coresCount)

        rawData.cameraList.forEach {
            sb
                .append(it.cameraName)
                .append(it.cameraType)
                .append(it.cameraOrientation)
        }

        // Optimal
        if ((stabilityLevel == StabilityLevel.OPTIMAL) or (stabilityLevel == StabilityLevel.UNIQUE)) {
            sb.append(rawData.batteryFullCapacity)
                .append(rawData.batteryHealth)
        }

        return sb.toString()
    }

    override fun rawData() = rawData
}