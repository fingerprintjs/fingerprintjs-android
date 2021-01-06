package com.fingerprintjs.android.fingerprint.signal_providers


import com.fingerprintjs.android.fingerprint.info_providers.BatteryInfoDataSource
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.info_providers.MemInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.SensorData
import com.fingerprintjs.android.fingerprint.info_providers.SensorDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test


class HardwareSignalGroupProviderTests {
    @Test
    fun `HardwareFingerprinter v1 test - success`() {

        val cpuInfoProvider = object :
            CpuInfoProvider {
            override fun cpuInfo() = mapOf("cpu" to "intel")
            override fun abiType(): String {
                TODO("Not yet implemented")
            }

            override fun coresCount(): String {
                TODO("Not yet implemented")
            }
        }

        val memInfoProvider = object :
            MemInfoProvider {
            override fun totalRAM() = 1024L
            override fun totalInternalStorageSpace() = 1024L
            override fun totalExternalStorageSpace() = 1024L
        }

        val osBuildInfoProvider = object :
            OsBuildInfoProvider {
            override fun modelName() = "model"
            override fun manufacturerName() = "manufacturer"
            override fun androidVersion(): String {
                TODO("Not yet implemented")
            }

            override fun sdkVersion(): String {
                TODO("Not yet implemented")
            }

            override fun kernelVersion(): String {
                TODO("Not yet implemented")
            }

            override fun fingerprint() = ""
        }

        val sensorDataSource = object :
            SensorDataSource {
            override fun sensors() = listOf(
                SensorData(
                    "sensorName",
                    "vendorName"
                )
            )
        }

        val inputDeviceDataSource = object :
            InputDeviceDataSource {
            override fun getInputDeviceData() = listOf(
                InputDeviceData(
                    "inputDeviceName",
                    "vendorName"
                )
            )
        }

        val batteryInfoDataSource = object : BatteryInfoDataSource {
            override fun batteryHealth(): String {
                TODO("Not yet implemented")
            }

            override fun batteryTotalCapacity(): String {
                TODO("Not yet implemented")
            }

        }

        val cameraInfoProvider = object : CameraInfoProvider {
            override fun getCameraInfo(): List<CameraInfo> {
                TODO("Not yet implemented")
            }

        }

        val fingerprinter =
            HardwareSignalGroupProvider(
                cpuInfoProvider = cpuInfoProvider,
                memInfoProvider = memInfoProvider,
                osBuildInfoProvider = osBuildInfoProvider,
                sensorsDataSource = sensorDataSource,
                inputDeviceDataSource = inputDeviceDataSource,
                batteryInfoDataSource = batteryInfoDataSource,
                cameraInfoProvider = cameraInfoProvider,
                hasher = EmptyHasher(),
                version = 1
            )

        assertEquals("manufacturermodel10241024cpuintelsensorNamevendorNameinputDeviceNamevendorName", fingerprinter.fingerprint())
    }
}