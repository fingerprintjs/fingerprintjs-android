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
import com.fingerprintjs.android.fingerprint.info_providers.BatteryInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.CodecInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.DevicePersonalizationInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.DeviceSecurityInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.FingerprintSensorInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.GpuInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.InputDevicesDataSourceImpl
import com.fingerprintjs.android.fingerprint.info_providers.MemInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.MemInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.PackageManagerDataSourceImpl
import com.fingerprintjs.android.fingerprint.info_providers.SensorDataSourceImpl
import com.fingerprintjs.android.fingerprint.info_providers.SettingsDataSourceImpl
import com.fingerprintjs.android.fingerprint.signal_providers.device_id.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher


object FingerprinterFactory {

    private var configuration: Configuration = Configuration(version = 3)
    private var instance: Fingerprinter? = null
    private var hasher: Hasher = MurMur3x64x128Hasher()

    @JvmStatic
    fun getInstance(
        context: Context,
        configuration: Configuration
    ): Fingerprinter {
        if (this.configuration != configuration) {
            instance = null
        }

        if (instance == null) {
            synchronized(FingerprinterFactory::class.java) {
                if (instance == null) {
                    instance = initializeFingerprinter(context, configuration)
                }
            }
        }

        return instance!!
    }

    private fun initializeFingerprinter(
        context: Context,
        configuration: Configuration
    ): Fingerprinter {
        this.configuration = configuration
        this.hasher = configuration.hasher

        return FingerprinterImpl(
            createHardwareSignalGroupProvider(context),
            createOsBuildInfoSignalGroupProvider(context),
            createDeviceIdProvider(context),
            createInstalledApplicationsSignalGroupProvider(context),
            createDeviceStateSignalGroupProvider(context),
            configuration
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

        val externalStorageDir = context.getExternalFilesDir(null)?.absolutePath
        val externalStorageStatFs =
            if (externalStorageDir != null) StatFs(externalStorageDir) else null

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
}