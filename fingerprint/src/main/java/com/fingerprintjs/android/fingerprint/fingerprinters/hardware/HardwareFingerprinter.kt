package com.fingerprintjs.android.fingerprint.fingerprinters.hardware


import com.fingerprintjs.android.fingerprint.datasources.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.datasources.MemInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.SensorDataSource
import com.fingerprintjs.android.fingerprint.fingerprinters.Fingerprinter
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class HardwareFingerprinter(
    cpuInfoProvider: CpuInfoProvider,
    memInfoProvider: MemInfoProvider,
    osBuildInfoProvider: OsBuildInfoProvider,
    sensorsDataSource: SensorDataSource,
    inputDeviceDataSource: InputDeviceDataSource,
    private val hasher: Hasher,
    version: Int
) : Fingerprinter<HardwareFingerprintRawData>(
    version
) {
    private val rawData = HardwareFingerprintRawData(
        osBuildInfoProvider.manufacturerName(),
        osBuildInfoProvider.modelName(),
        memInfoProvider.totalRAM(),
        memInfoProvider.totalInternalStorageSpace(),
        cpuInfoProvider.cpuInfo(),
        sensorsDataSource.sensors(),
        inputDeviceDataSource.getInputDeviceData()
    )

    override fun calculate(): String {
        return when (version) {
            1 -> v1()
            else -> v1()
        }
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

        return hasher.hash(sb.toString())
    }

    override fun rawData() = rawData
}