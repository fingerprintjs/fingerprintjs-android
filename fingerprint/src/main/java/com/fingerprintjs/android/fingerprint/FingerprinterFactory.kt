package com.fingerprintjs.android.fingerprint


import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.hardware.SensorManager
import android.hardware.input.InputManager
import android.media.RingtoneManager
import android.os.Environment
import android.os.StatFs
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.fingerprintjs.android.fingerprint.datasources.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.CpuInfoProviderImpl
import com.fingerprintjs.android.fingerprint.datasources.DevicePersonalizationDataSource
import com.fingerprintjs.android.fingerprint.datasources.DevicePersonalizationDataSourceImpl
import com.fingerprintjs.android.fingerprint.datasources.FingerprintSensorInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.FingerprintSensorInfoProviderImpl
import com.fingerprintjs.android.fingerprint.datasources.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.datasources.InputDevicesDataSourceImpl
import com.fingerprintjs.android.fingerprint.datasources.KeyGuardInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.KeyGuardInfoProviderImpl
import com.fingerprintjs.android.fingerprint.datasources.MemInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.MemInfoProviderImpl
import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProviderImpl
import com.fingerprintjs.android.fingerprint.datasources.PackageManagerDataSource
import com.fingerprintjs.android.fingerprint.datasources.PackageManagerDataSourceImpl
import com.fingerprintjs.android.fingerprint.datasources.SensorDataSource
import com.fingerprintjs.android.fingerprint.datasources.SensorDataSourceImpl
import com.fingerprintjs.android.fingerprint.datasources.SettingsDataSource
import com.fingerprintjs.android.fingerprint.datasources.SettingsDataSourceImpl
import com.fingerprintjs.android.fingerprint.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProviderImpl
import com.fingerprintjs.android.fingerprint.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher


object FingerprinterFactory {

    private var configuration: Configuration = Configuration(version = 1)
    private var instance: Fingerprinter? = null
    private var hasher: Hasher = MurMur3x64x128Hasher()

    private lateinit var context: Context

    @JvmStatic
    @JvmOverloads
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
        this.context = context
        this.hasher = configuration.hasher

        return FingerprinterImpl(
            createHardwareFingerprinter(),
            createOsBuildInfoFingerprinter(),
            createDeviceIdProvider(),
            createInstalledApplicationsFingerprinter(),
            createDeviceStateFingerprinter(),
            configuration
        )
    }

    //region:Fingerprinters

    private fun createHardwareFingerprinter(): HardwareSignalProvider {
        return HardwareSignalProvider(
            createCpuInfoProvider(),
            createMemoryInfoProvider(),
            createOsBuildInfoProvider(),
            createSensorDataSource(),
            createInputDevicesDataSource(),
            hasher,
            configuration.version
        )
    }

    private fun createOsBuildInfoFingerprinter(): OsBuildSignalProvider {
        return OsBuildSignalProvider(
            createOsBuildInfoProvider(),
            hasher,
            configuration.version
        )
    }

    private fun createInstalledApplicationsFingerprinter(): InstalledAppsSignalProvider {
        return InstalledAppsSignalProvider(
            createPackageManagerDataSource(),
            hasher,
            configuration.version
        )
    }

    private fun createDeviceStateFingerprinter(): DeviceStateSignalProvider {
        return DeviceStateSignalProvider(
            createSettingsDataSource(),
            createDevicePersonalizationDataSource(),
            createKeyGuardInfoProvider(),
            createFingerprintSensorStatusProvider(),
            hasher,
            configuration.version
        )
    }

    private fun createDeviceIdProvider(): DeviceIdProvider {
        return DeviceIdProviderImpl(
            createGsfIdProvider(),
            createAndroidIdProvider()
        )
    }

    //endregion

    //region:DataSources
    private fun createCpuInfoProvider(): CpuInfoProvider {
        return CpuInfoProviderImpl()
    }

    private fun createMemoryInfoProvider(): MemInfoProvider {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val internalStorageStatFs = StatFs(Environment.getRootDirectory().absolutePath)
        val externalStorageStatFs = StatFs(context.getExternalFilesDir(null)?.absolutePath!!)
        return MemInfoProviderImpl(activityManager, internalStorageStatFs, externalStorageStatFs)
    }

    private fun createOsBuildInfoProvider(): OsBuildInfoProvider {
        return OsBuildInfoProviderImpl()
    }

    private fun createGsfIdProvider(): GsfIdProvider {
        return GsfIdProvider(context.contentResolver!!)
    }

    private fun createAndroidIdProvider(): AndroidIdProvider {
        return AndroidIdProvider(context.contentResolver!!)
    }

    private fun createSensorDataSource(): SensorDataSource {
        return SensorDataSourceImpl(
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        )
    }

    private fun createInputDevicesDataSource(): InputDeviceDataSource {
        return InputDevicesDataSourceImpl(
            context.getSystemService(Context.INPUT_SERVICE) as InputManager
        )
    }

    private fun createPackageManagerDataSource(): PackageManagerDataSource {
        return PackageManagerDataSourceImpl(
            context.packageManager
        )
    }

    private fun createSettingsDataSource(): SettingsDataSource {
        return SettingsDataSourceImpl(context.contentResolver)
    }


    private fun createDevicePersonalizationDataSource(): DevicePersonalizationDataSource {
        return DevicePersonalizationDataSourceImpl(
            RingtoneManager(context),
            context.assets
        )
    }

    private fun createFingerprintSensorStatusProvider(): FingerprintSensorInfoProvider {
        return FingerprintSensorInfoProviderImpl(
            FingerprintManagerCompat.from(context)
        )
    }

    private fun createKeyGuardInfoProvider(): KeyGuardInfoProvider {
        return KeyGuardInfoProviderImpl(
            context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        )
    }

    //endregion
}