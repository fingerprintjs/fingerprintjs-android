package com.fingerprintjs.android.fingerprint.signal_providers


import com.fingerprintjs.android.fingerprint.datasources.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.InputDeviceData
import com.fingerprintjs.android.fingerprint.datasources.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.datasources.MemInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.SensorData
import com.fingerprintjs.android.fingerprint.datasources.SensorDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test


class HardwareSignalProviderTests {
    @Test
    fun `HardwareFingerprinter v1 test - success`() {

        val cpuInfoProvider = object : CpuInfoProvider {
            override fun cpuInfo() = mapOf("cpu" to "intel")
        }

        val memInfoProvider = object : MemInfoProvider {
            override fun totalRAM() = 1024L
            override fun totalInternalStorageSpace() = 1024L
            override fun totalExternalStorageSpace() = 1024L
        }

        val osBuildInfoProvider = object : OsBuildInfoProvider {
            override fun modelName() = "model"
            override fun manufacturerName() = "manufacturer"
            override fun fingerprint() = ""
        }

        val sensorDataSource = object : SensorDataSource {
            override fun sensors() = listOf(SensorData("sensorName", "vendorName"))
        }

        val inputDeviceDataSource = object : InputDeviceDataSource {
            override fun getInputDeviceData() = listOf(InputDeviceData("inputDeviceName", "vendorName"))

        }
        val fingerprinter =
            HardwareSignalProvider(
                cpuInfoProvider = cpuInfoProvider,
                memInfoProvider = memInfoProvider,
                osBuildInfoProvider = osBuildInfoProvider,
                sensorsDataSource = sensorDataSource,
                inputDeviceDataSource = inputDeviceDataSource,
                hasher = EmptyHasher(),
                version = 1
            )

        assertEquals("manufacturermodel10241024cpuintelsensorNamevendorNameinputDeviceNamevendorName", fingerprinter.fingerprint())
    }
}