package com.fingerprintjs.android.fingerprint.info_providers


import com.fingerprintjs.android.fingerprint.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.device_id.DeviceIdProvider
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Test


// Check that there is no unhandled exceptions. Calling mock methods produces NPE.
class InfoProvidersExceptionsHandlingTests {
    @Test
    fun `CPUInfo provider crash free`() {
        val cpuInfoProvider =
            CpuInfoProviderImpl()
        assertNotNull(cpuInfoProvider.cpuInfo().size)
        assertNotNull(cpuInfoProvider.abiType())
        assertNotNull(cpuInfoProvider.coresCount())
    }

    @Test
    fun `Memory Info provider crash free`() {
        val memInfoProvider =
            MemInfoProviderImpl(
                mock(),
                mock(),
                mock()
            )

        assertNotNull(memInfoProvider.totalRAM())
        assertNotNull(memInfoProvider.totalExternalStorageSpace())
        assertNotNull(memInfoProvider.totalInternalStorageSpace())
    }

    @Test
    fun `DevicePersonalizationDataSource crash free`() {
        val devicePersonalizationDataSource =
            DevicePersonalizationInfoProviderImpl(
                mock(),
                mock(),
                mock()
            )

        assertNotNull(devicePersonalizationDataSource.ringtoneSource())
        assertNotNull(devicePersonalizationDataSource.availableLocales().size)
        assertNotNull(devicePersonalizationDataSource.defaultLanguage())

    }

    @Test
    fun `InputDeviceDataSource crash free`() {
        val inputDeviceDatasource =
            InputDevicesDataSourceImpl(
                mock()
            )
        assertNotNull(inputDeviceDatasource.getInputDeviceData().size)
    }

    @Test
    fun `SensorsDataSource crash free`() {
        val sensorsDataSource =
            SensorDataSourceImpl(
                mock()
            )
        assertNotNull(sensorsDataSource.sensors().size)
    }

    @Test
    fun `PackageManager datasource crash free`() {
        val packageManagerDataSource =
            PackageManagerDataSourceImpl(
                mock()
            )
        assertNotNull(packageManagerDataSource.getApplicationsList())
        assertNotNull(packageManagerDataSource.getSystemApplicationsList())
    }

    @Test
    fun `SettingsDataSource crash free`() {
        val settingsDataSource =
            SettingsDataSourceImpl(
                mock()
            )
        assertEquals("", settingsDataSource.accessibilityEnabled())
        assertEquals("", settingsDataSource.adbEnabled())
        assertEquals("", settingsDataSource.alarmAlertPath())
        assertEquals("", settingsDataSource.dataRoamingEnabled())
        assertEquals("", settingsDataSource.dateFormat())
        assertEquals("", settingsDataSource.defaultInputMethod())
        assertEquals("", settingsDataSource.developmentSettingsEnabled())
        assertEquals("", settingsDataSource.endButtonBehaviour())
        assertEquals("", settingsDataSource.fontScale())
        assertEquals("", settingsDataSource.httpProxy())
        assertEquals("", settingsDataSource.rttCallingMode())
        assertEquals("", settingsDataSource.screenOffTimeout())
        assertEquals("", settingsDataSource.textAutoPunctuate())
        assertEquals("", settingsDataSource.textAutoReplaceEnable())
        assertEquals("", settingsDataSource.time12Or24())
        assertEquals("", settingsDataSource.touchExplorationEnabled())
        assertEquals("", settingsDataSource.transitionAnimationScale())
        assertEquals("", settingsDataSource.windowAnimationScale())

    }

    @Test
    fun `DeviceId crash free`() {
        val deviceIdProvider = DeviceIdProvider(
            GsfIdProvider(mock()),
            AndroidIdProvider(mock()),
            MediaDrmIdProvider(),
            2
        )
        assertEquals("", deviceIdProvider.fingerprint())
    }

    @Test
    fun `BatteryInfoProvider crash free`() {
        val batteryInfoProvider = BatteryInfoProviderImpl(mock())
        assertNotNull(batteryInfoProvider.batteryHealth())
        assertNotNull(batteryInfoProvider.batteryTotalCapacity())
    }

    @Test
    fun `CameraInfoProvider crash free`() {
        val cameraInfoProviderImpl = CameraInfoProviderImpl()
        assertNotNull(cameraInfoProviderImpl.getCameraInfo())
    }

    @Test
    fun `CodecInfoProvider crash free`() {
        val codecInfoProvider = CodecInfoProviderImpl(mock())
        assertNotNull(codecInfoProvider.codecsList())
    }

    @Test
    fun `GpuInfo provider`() {
        val gpuInfoProvider = GpuInfoProviderImpl(mock())
        assertNotNull(gpuInfoProvider.glesVersion())
    }

    @Test
    fun `DeviceSecurityInfoProvider crash free`() {
        val deviceSecurityInfoProvider = DeviceSecurityInfoProviderImpl(mock(), mock())
        assertNotNull(deviceSecurityInfoProvider.encryptionStatus())
        assertNotNull(deviceSecurityInfoProvider.isPinSecurityEnabled())
        assertNotNull(deviceSecurityInfoProvider.securityProvidersData())
    }

    @Test
    fun `DevicePersonalizationInfoProvider crash free`() {
        val devicePersonalizationDataSource =
            DevicePersonalizationInfoProviderImpl(mock(), mock(), mock())
        assertNotNull(devicePersonalizationDataSource.availableLocales())
        assertNotNull(devicePersonalizationDataSource.defaultLanguage())
        assertNotNull(devicePersonalizationDataSource.regionCountry())
        assertNotNull(devicePersonalizationDataSource.timezone())
        assertNotNull(devicePersonalizationDataSource.ringtoneSource())
    }
}