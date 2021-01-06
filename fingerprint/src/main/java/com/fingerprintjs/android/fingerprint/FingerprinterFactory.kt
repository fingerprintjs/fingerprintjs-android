@file:Suppress("DEPRECATION")

package com.fingerprintjs.android.fingerprint


import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.hardware.SensorManager
import android.hardware.input.InputManager
import android.media.MediaCodecList
import android.media.RingtoneManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.fingerprintjs.android.fingerprint.info_providers.BatteryInfoDataSource
import com.fingerprintjs.android.fingerprint.info_providers.BatteryInfoDataSourceImpl
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.CodecInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CodecInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.DevicePersonalizationDataSource
import com.fingerprintjs.android.fingerprint.info_providers.DevicePersonalizationDataSourceImpl
import com.fingerprintjs.android.fingerprint.info_providers.FingerprintSensorInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.FingerprintSensorInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.info_providers.InputDevicesDataSourceImpl
import com.fingerprintjs.android.fingerprint.info_providers.KeyGuardInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.KeyGuardInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.MemInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.MemInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProviderImpl
import com.fingerprintjs.android.fingerprint.info_providers.PackageManagerDataSource
import com.fingerprintjs.android.fingerprint.info_providers.PackageManagerDataSourceImpl
import com.fingerprintjs.android.fingerprint.info_providers.SensorDataSource
import com.fingerprintjs.android.fingerprint.info_providers.SensorDataSourceImpl
import com.fingerprintjs.android.fingerprint.info_providers.SettingsDataSource
import com.fingerprintjs.android.fingerprint.info_providers.SettingsDataSourceImpl
import com.fingerprintjs.android.fingerprint.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProviderImpl
import com.fingerprintjs.android.fingerprint.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher


object FingerprinterFactory {

    private var configuration: Configuration = Configuration(version = 1)
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
            createHardwareFingerprinter(context),
            createOsBuildInfoFingerprinter(),
            createDeviceIdProvider(context),
            createInstalledApplicationsFingerprinter(context),
            createDeviceStateFingerprinter(context),
            configuration
        )
    }

    //region:Fingerprinters

    private fun createHardwareFingerprinter(context: Context): HardwareSignalGroupProvider {
        return HardwareSignalGroupProvider(
            createCpuInfoProvider(),
            createMemoryInfoProvider(context),
            createOsBuildInfoProvider(),
            createSensorDataSource(context),
            createInputDevicesDataSource(context),
            createBatteryInfoDataSource(context),
            createCameraInfoProvider(context),
            hasher,
            configuration.version
        )
    }

    private fun createOsBuildInfoFingerprinter(): OsBuildSignalGroupProvider {
        return OsBuildSignalGroupProvider(
            createOsBuildInfoProvider(),
            createCodecInfoProvider(),
            hasher,
            configuration.version
        )
    }

    private fun createInstalledApplicationsFingerprinter(context: Context): InstalledAppsSignalGroupProvider {
        return InstalledAppsSignalGroupProvider(
            createPackageManagerDataSource(context),
            hasher,
            configuration.version
        )
    }

    private fun createDeviceStateFingerprinter(context: Context): DeviceStateSignalGroupProvider {
        return DeviceStateSignalGroupProvider(
            createSettingsDataSource(context),
            createDevicePersonalizationDataSource(context),
            createKeyGuardInfoProvider(context),
            createFingerprintSensorStatusProvider(context),
            hasher,
            configuration.version
        )
    }

    private fun createDeviceIdProvider(context: Context): DeviceIdProvider {
        return DeviceIdProviderImpl(
            createGsfIdProvider(context),
            createAndroidIdProvider(context)
        )
    }

    //endregion

    //region:DataSources
    private fun createCpuInfoProvider(): CpuInfoProvider {
        return CpuInfoProviderImpl()
    }

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

    private fun createOsBuildInfoProvider(): OsBuildInfoProvider {
        return OsBuildInfoProviderImpl()
    }

    private fun createGsfIdProvider(context: Context): GsfIdProvider {
        return GsfIdProvider(context.contentResolver!!)
    }

    private fun createAndroidIdProvider(context: Context): AndroidIdProvider {
        return AndroidIdProvider(context.contentResolver!!)
    }

    private fun createSensorDataSource(context: Context): SensorDataSource {
        return SensorDataSourceImpl(
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        )
    }

    private fun createInputDevicesDataSource(context: Context): InputDeviceDataSource {
        return InputDevicesDataSourceImpl(
            context.getSystemService(Context.INPUT_SERVICE) as InputManager
        )
    }

    private fun createPackageManagerDataSource(context: Context): PackageManagerDataSource {
        return PackageManagerDataSourceImpl(
            context.packageManager
        )
    }

    private fun createSettingsDataSource(context: Context): SettingsDataSource {
        return SettingsDataSourceImpl(
            context.contentResolver
        )
    }


    private fun createDevicePersonalizationDataSource(context: Context): DevicePersonalizationDataSource {
        return DevicePersonalizationDataSourceImpl(
            RingtoneManager(context),
            context.assets
        )
    }

    private fun createFingerprintSensorStatusProvider(context: Context): FingerprintSensorInfoProvider {
        return FingerprintSensorInfoProviderImpl(
            FingerprintManagerCompat.from(context)
        )
    }

    private fun createKeyGuardInfoProvider(context: Context): KeyGuardInfoProvider {
        return KeyGuardInfoProviderImpl(
            context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        )
    }

    private fun createCodecInfoProvider(): CodecInfoProvider? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CodecInfoProviderImpl(
                MediaCodecList(MediaCodecList.ALL_CODECS)
            )
        } else {
            null
        }
    }

    private fun createBatteryInfoDataSource(context: Context): BatteryInfoDataSource {
        return BatteryInfoDataSourceImpl(
            context
        )
    }

    private fun createCameraInfoProvider(context: Context): CameraInfoProvider {
        return CameraInfoProviderImpl()
    }

    //endregion
}