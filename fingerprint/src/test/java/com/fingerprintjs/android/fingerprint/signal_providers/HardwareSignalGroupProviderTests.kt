package com.fingerprintjs.android.fingerprint.signal_providers


import com.fingerprintjs.android.fingerprint.info_providers.BatteryInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.GpuInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.info_providers.MemInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.SensorData
import com.fingerprintjs.android.fingerprint.info_providers.SensorDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfo
import junit.framework.Assert.assertEquals
import org.junit.Test


class HardwareSignalGroupProviderTests {
    @Test
    fun `HardwareFingerprinter v1 test - success`() {
        val signalGroupProvider = prepareHardwareSignalGroupProvider(1)
        assertEquals(
            "manufacturermodel10241024cpuintelsensorNamevendorNameinputDeviceNamevendorName",
            signalGroupProvider.fingerprint()
        )
    }

    @Test
    fun `HardwareFingerprinter v2 test - success`() {
        val signalGroupProvider = prepareHardwareSignalGroupProvider(2)
        assertEquals(
            "manufacturermodel10241024cpuintelsensorNamevendorNameinputDeviceNamevendorName3700good1.00armV780frontalvertical1backhorizontal",
            signalGroupProvider.fingerprint()
        )
    }

    @Test
    fun `HardwareFingerprinter v4 test - success`() {
        val signalGroupProvider = prepareHardwareSignalGroupProvider(4)
        assertEquals(
            "manufacturermodel10241024[(Hardware, Qualcomm Technologies, Inc SM6125)][[]]sensorNamevendorNameinputDeviceNamevendorNamegood37000frontalvertical1backhorizontal1.00armV78",
            signalGroupProvider.fingerprint()
        )
    }
}

private val fakeCpuInfo = CpuInfo(
    commonInfo = listOf(
        "Processor" to "ARM",
        "Hardware" to "Qualcomm Technologies, Inc SM6125",
    ),
    perProcessorInfo = listOf(
        listOf(
            "bogomips" to "7000",
            "BogoMIPS" to "7000",
            "cpu MHz" to "3593.843",
        )
    )
)

private fun prepareHardwareSignalGroupProvider(version: Int): HardwareSignalGroupProvider {

    val cpuInfoProvider = object : CpuInfoProvider {
        override fun cpuInfo() = mapOf("cpu" to "intel")
        override fun cpuInfoV2() = fakeCpuInfo
        override fun abiType() = "armV7"

        override fun coresCount() = 8
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
        override fun androidVersion() = "11"

        override fun sdkVersion() = "30"

        override fun kernelVersion() = "5.11"

        override fun fingerprint() = "fingerprint"
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

    val batteryInfoDataSource = object : BatteryInfoProvider {
        override fun batteryHealth() = "good"

        override fun batteryTotalCapacity() = "3700"

    }

    val cameraInfoProvider = object : CameraInfoProvider {
        override fun getCameraInfo() = listOf(
            CameraInfo("0", "frontal", "vertical"),
            CameraInfo("1", "back", "horizontal")
        )

    }

    val gpuInfoProvider = object : GpuInfoProvider {
        override fun glesVersion() = "1.00"
    }

    return HardwareSignalGroupProvider(
        cpuInfoProvider = cpuInfoProvider,
        memInfoProvider = memInfoProvider,
        osBuildInfoProvider = osBuildInfoProvider,
        sensorsDataSource = sensorDataSource,
        inputDeviceDataSource = inputDeviceDataSource,
        batteryInfoProvider = batteryInfoDataSource,
        cameraInfoProvider = cameraInfoProvider,
        gpuInfoProvider = gpuInfoProvider,
        hasher = EmptyHasher(),
        version = version
    )

}