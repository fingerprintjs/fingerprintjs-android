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
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public class HardwareSignalGroupProvider(
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
    private val rawData by lazy {
        HardwareFingerprintRawData(
            osBuildInfoProvider.manufacturerName(),
            osBuildInfoProvider.modelName(),
            memInfoProvider.totalRAM(),
            memInfoProvider.totalInternalStorageSpace(),
            cpuInfoProvider.cpuInfo(),
            cpuInfoProvider.cpuInfoV2(),
            sensorsDataSource.sensors(),
            inputDeviceDataSource.getInputDeviceData(),
            batteryInfoProvider.batteryHealth(),
            batteryInfoProvider.batteryTotalCapacity(),
            cameraInfoProvider.getCameraInfo(),
            gpuInfoProvider.glesVersion(),
            cpuInfoProvider.abiType(),
            cpuInfoProvider.coresCount()
        )
    }

    override fun fingerprint(stabilityLevel: StabilityLevel): String {
        return hasher.hash(
            combineSignals(
                when (version) {
                    1 -> v1()
                    2, 3 -> v2()
                    else -> rawData.signals(version, stabilityLevel)
                }, stabilityLevel
            )
        )
    }

    private fun v1() = listOf(
        rawData.manufacturerName(),
        rawData.modelName(),
        rawData.totalRAM(),
        rawData.totalInternalStorageSpace(),
        rawData.procCpuInfo(),
        rawData.sensors(),
        rawData.inputDevices()
    )

    private fun v2() = listOf(
        rawData.manufacturerName(),
        rawData.modelName(),
        rawData.totalRAM(),
        rawData.totalInternalStorageSpace(),
        rawData.procCpuInfo(),
        rawData.sensors(),
        rawData.inputDevices(),
        rawData.batteryFullCapacity(),
        rawData.batteryHealth(),
        rawData.glesVersion(),
        rawData.abiType(),
        rawData.coresCount(),
        rawData.cameraList()
    )

    override fun rawData(): HardwareFingerprintRawData = rawData
}