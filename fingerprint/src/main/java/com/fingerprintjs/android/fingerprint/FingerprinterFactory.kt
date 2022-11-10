@file:Suppress("DEPRECATION")

package com.fingerprintjs.android.fingerprint


import android.app.ActivityManager
import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.hardware.SensorManager
import android.hardware.input.InputManager
import android.media.MediaCodecList
import android.media.RingtoneManager
import android.os.Environment
import android.os.StatFs
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.fingerprintjs.android.fingerprint.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fingerprint.device_id_signals.DeviceIdSignalsProvider
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.FingerprintingSignalsProvider
import com.fingerprintjs.android.fingerprint.info_providers.*
import com.fingerprintjs.android.fingerprint.signal_providers.device_id.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher


/**
 * A factory for [Fingerprinter] class.
 */
public object FingerprinterFactory {

    private var configuration: Configuration = Configuration(version = Fingerprinter.Version.fingerprintingGroupedSignalsLastVersion.intValue)
    private var instance: Fingerprinter? = null
    private var hasher: Hasher = MurMur3x64x128Hasher()

    /**
     * A factory method for the [Fingerprinter] class. Consecutive calls to this method with the same [configuration] will return
     * the same instance.
     *
     * This method has been deprecated for multiple reasons:
     * - [Configuration] is not needed for the newer API. Check [Fingerprinter]'s getFingerprint(stabilityLevel, listener) method
     * for an explanation.
     * - This method does not allow to reinstantiate [Fingerprinter] with the same [Configuration], which could be useful
     * if you ever wanted to re-run fingerprint or device id evaluation for some reason.
     *
     * @param context Android context.
     * @param configuration [Configuration] for [Fingerprinter]
     * @throws [IllegalArgumentException] if [Configuration.version] is higher than [Fingerprinter.Version.fingerprintingGroupedSignalsLastVersion]
     */
    @JvmStatic
    @Deprecated(message = """
        This method has been deprecated in favor of create(context) method. Check out method doc for details.
    """)
    @Throws(IllegalArgumentException::class)
    public fun getInstance(
        context: Context,
        configuration: Configuration
    ): Fingerprinter {
        if (configuration.version > Fingerprinter.Version.fingerprintingGroupedSignalsLastVersion.intValue) {
            throw IllegalArgumentException(
                "Version must be in " +
                        "${Fingerprinter.Version.V_1.intValue} .. ${Fingerprinter.Version.fingerprintingGroupedSignalsLastVersion.intValue} range"
            )
        }

        if (this.configuration != configuration) {
            instance = null
        }

        if (instance == null) {
            synchronized(FingerprinterFactory::class.java) {
                if (instance == null) {
                    instance = createLegacyFingerprinter(context, configuration)
                }
            }
        }

        return instance!!
    }

    /**
     * A factory method for the [Fingerprinter] class.
     *
     * @param context Android context.
     */
    @JvmStatic
    public fun create(
        context: Context,
    ): Fingerprinter {
        return createFingerprinter(context)
    }

    private fun createLegacyFingerprinter(
        context: Context,
        configuration: Configuration
    ): Fingerprinter {
        this.configuration = configuration
        this.hasher = configuration.hasher

        return Fingerprinter(
            legacyArgs = Fingerprinter.LegacyArgs(
                hardwareSignalProvider = createHardwareSignalGroupProvider(context),
                osBuildSignalProvider = createOsBuildInfoSignalGroupProvider(context),
                deviceIdProvider = createDeviceIdProvider(context),
                installedAppsSignalProvider = createInstalledApplicationsSignalGroupProvider(context),
                deviceStateSignalProvider = createDeviceStateSignalGroupProvider(context),
                configuration = configuration,
            ),
            fpSignalsProvider = createFingerprintingSignalsProvider(context),
            deviceIdSignalsProvider = createDeviceIdSignalsProvider(context),
        )
    }

    private fun createFingerprinter(
        context: Context,
    ): Fingerprinter {
        return Fingerprinter(
            legacyArgs = null,
            fpSignalsProvider = createFingerprintingSignalsProvider(context),
            deviceIdSignalsProvider = createDeviceIdSignalsProvider(context),
        )
    }

    //region:Signal group providers

    private fun createHardwareSignalGroupProvider(context: Context) = HardwareSignalGroupProvider(
        createCpuInfoProvider(),
        createMemoryInfoProvider(context),
        createOsBuildInfoProvider(),
        createSensorDataSource(context),
        createInputDevicesDataSource(context),
        createBatteryInfoDataSource(context),
        createCameraInfoProvider(),
        createGpuInfoProvider(context),
        hasher,
        configuration.version
    )

    private fun createOsBuildInfoSignalGroupProvider(context: Context) = OsBuildSignalGroupProvider(
        createOsBuildInfoProvider(),
        createCodecInfoProvider(),
        createDeviceSecurityProvider(context),
        hasher,
        configuration.version
    )

    private fun createInstalledApplicationsSignalGroupProvider(context: Context) =
        InstalledAppsSignalGroupProvider(
            createPackageManagerDataSource(context),
            hasher,
            configuration.version
        )

    private fun createDeviceStateSignalGroupProvider(context: Context) =
        DeviceStateSignalGroupProvider(
            createSettingsDataSource(context),
            createDevicePersonalizationDataSource(context),
            createDeviceSecurityProvider(context),
            createFingerprintSensorStatusProvider(context),
            hasher,
            configuration.version
        )

    private fun createDeviceIdProvider(context: Context) = DeviceIdProvider(
        createGsfIdProvider(context),
        createAndroidIdProvider(context),
        createMediaDrmProvider(),
        configuration.version
    )

    //endregion

    //region:Info providers
    private fun createCpuInfoProvider() = CpuInfoProviderImpl()

    private fun createMemoryInfoProvider(context: Context): MemInfoProvider {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val internalStorageDir = Environment.getRootDirectory().absolutePath
        val internalStorageStatFs = StatFs(internalStorageDir)

        val externalStorageDir = context.getExternalFilesDir(null)
        val externalStorageDirPath = externalStorageDir?.absolutePath
        val externalStorageStatFs =
            if (externalStorageDirPath != null && externalStorageDir.canRead()) {
                StatFs(externalStorageDirPath)
            } else {
                null
            }

        return MemInfoProviderImpl(
            activityManager,
            internalStorageStatFs,
            externalStorageStatFs
        )
    }

    private fun createOsBuildInfoProvider() = OsBuildInfoProviderImpl()

    private fun createGsfIdProvider(context: Context) = GsfIdProvider(context.contentResolver!!)

    private fun createMediaDrmProvider() = MediaDrmIdProvider()

    private fun createAndroidIdProvider(context: Context) =
        AndroidIdProvider(context.contentResolver!!)

    private fun createSensorDataSource(context: Context) = SensorDataSourceImpl(
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    )

    private fun createInputDevicesDataSource(context: Context) = InputDevicesDataSourceImpl(
        context.getSystemService(Context.INPUT_SERVICE) as InputManager
    )

    private fun createPackageManagerDataSource(context: Context) = PackageManagerDataSourceImpl(
        context.packageManager
    )

    private fun createSettingsDataSource(context: Context) = SettingsDataSourceImpl(
        context.contentResolver
    )


    private fun createDevicePersonalizationDataSource(context: Context) =
        DevicePersonalizationInfoProviderImpl(
            RingtoneManager(context),
            context.assets,
            context.resources.configuration
        )

    private fun createFingerprintSensorStatusProvider(context: Context) =
        FingerprintSensorInfoProviderImpl(
            FingerprintManagerCompat.from(context)
        )

    private fun createDeviceSecurityProvider(context: Context) = DeviceSecurityInfoProviderImpl(
        context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? DevicePolicyManager,
        context.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
    )

    private fun createCodecInfoProvider() = CodecInfoProviderImpl(
        MediaCodecList(MediaCodecList.ALL_CODECS)
    )

    private fun createBatteryInfoDataSource(context: Context) = BatteryInfoProviderImpl(
        context
    )

    private fun createCameraInfoProvider(): CameraInfoProvider {
        return CameraInfoProviderImpl()
    }

    private fun createGpuInfoProvider(context: Context) =
        GpuInfoProviderImpl(context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)

    //endregion

    //region: Signal providers

    private fun createFingerprintingSignalsProvider(context: Context): FingerprintingSignalsProvider {
        return FingerprintingSignalsProvider(
            cpuInfoProvider = createCpuInfoProvider(),
            memInfoProvider = createMemoryInfoProvider(context),
            sensorsDataSource = createSensorDataSource(context),
            inputDeviceDataSource = createInputDevicesDataSource(context),
            batteryInfoProvider = createBatteryInfoDataSource(context),
            cameraInfoProvider = createCameraInfoProvider(),
            gpuInfoProvider = createGpuInfoProvider(context),
            osBuildInfoProvider = createOsBuildInfoProvider(),
            codecInfoProvider = createCodecInfoProvider(),
            deviceSecurityInfoProvider = createDeviceSecurityProvider(context),
            packageManagerDataSource = createPackageManagerDataSource(context),
            settingsDataSource = createSettingsDataSource(context),
            devicePersonalizationInfoProvider = createDevicePersonalizationDataSource(context),
            fingerprintSensorInfoProvider = createFingerprintSensorStatusProvider(context),
        )
    }

    private fun createDeviceIdSignalsProvider(context: Context): DeviceIdSignalsProvider {
        return DeviceIdSignalsProvider(
            gsfIdProvider = createGsfIdProvider(context),
            androidIdProvider = createAndroidIdProvider(context),
            mediaDrmIdProvider = createMediaDrmProvider(),
        )
    }

    //endregion
}