package com.fingerprintjs.android.fingerprint


import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.hardware.SensorManager
import android.hardware.input.InputManager
import android.media.RingtoneManager
import android.os.Build
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
import com.fingerprintjs.android.fingerprint.fingerprinters.device_state.DeviceStateFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.hardware.HardwareFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.installed_apps.InstalledAppsFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.os_build_fingerprint.OsBuildFingerprinter
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.HasherType
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher


object FingerprintAndroidAgentFactory {

    private var configuration: FingerprintAndroidConfiguration = defaultConfiguration()
    private var instance: FingerprintAndroidAgent? = null

    private lateinit var context: Context

    @JvmStatic
    fun getInitializedInstance(
        context: Context,
        configuration: FingerprintAndroidConfiguration = defaultConfiguration()
    ): FingerprintAndroidAgent {

        if (instance == null) {
            synchronized(FingerprintAndroidAgentFactory::class.java) {
                if (instance == null) {
                    instance = initializeFingerprintAndroidAgent(context, configuration)
                }
            }
        }

        return instance!!
    }

    private fun initializeFingerprintAndroidAgent(
        context: Context,
        configuration: FingerprintAndroidConfiguration
    ): FingerprintAndroidAgent {
        this.configuration = configuration
        this.context = context
        return FingerprintAndroidAgentImpl(
            createHardwareFingerprinter(),
            createOsBuildInfoFingerprinter(),
            createDeviceIdProvider(),
            createInstalledApplicationsFingerprinter(),
            createDeviceStateFingerprinter(),
            getHasherWithType()
        )
    }

    private fun defaultConfiguration(): FingerprintAndroidConfiguration {
        return FingerprintAndroidConfiguration(
            1,
            1,
            1,
            1,
            HasherType.MurMur3
        )
    }

    private fun getHasherWithType(): Hasher {
        return when (configuration.hasherType) {
            HasherType.MurMur3 -> {
                MurMur3x64x128Hasher()
            }
            else -> MurMur3x64x128Hasher()
        }
    }

    //region:Fingerprinters

    private fun createHardwareFingerprinter(): HardwareFingerprinter {
        return HardwareFingerprinter(
            createCpuInfoProvider(),
            createMemoryInfoProvider(),
            createOsBuildInfoProvider(),
            createSensorDataSource(),
            createInputDevicesDataSource(),
            getHasherWithType(),
            configuration.hardwareFingerprintVersion
        )
    }

    private fun createOsBuildInfoFingerprinter(): OsBuildFingerprinter {
        return OsBuildFingerprinter(
            createOsBuildInfoProvider(),
            getHasherWithType(),
            configuration.osBuildFingerprintVersion
        )
    }

    private fun createInstalledApplicationsFingerprinter(): InstalledAppsFingerprinter {
        return InstalledAppsFingerprinter(
            createPackageManagerDataSource(),
            getHasherWithType(),
            configuration.installedAppsFingerprintVersion
        )
    }

    private fun createDeviceStateFingerprinter(): DeviceStateFingerprinter {
        return DeviceStateFingerprinter(
            createSettingsDataSource(),
            createDevicePersonalizationDataSource(),
            createKeyGuardInfoProvider(),
            createFingerprintSensorStatusProvider(),
            getHasherWithType(),
            configuration.deviceStateFingerprintVersion
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