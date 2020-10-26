package com.fingerprintjs.android.fingerprint


import android.app.ActivityManager
import android.content.Context
import android.hardware.SensorManager
import android.hardware.input.InputManager
import android.os.Environment
import android.os.StatFs
import com.fingerprintjs.android.fingerprint.datasources.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.CpuInfoProviderImpl
import com.fingerprintjs.android.fingerprint.datasources.InputDeviceDataSource
import com.fingerprintjs.android.fingerprint.datasources.InputDevicesDataSourceImpl
import com.fingerprintjs.android.fingerprint.datasources.MemInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.MemInfoProviderImpl
import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProviderImpl
import com.fingerprintjs.android.fingerprint.datasources.SensorDataSource
import com.fingerprintjs.android.fingerprint.datasources.SensorDataSourceImpl
import com.fingerprintjs.android.fingerprint.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProviderImpl
import com.fingerprintjs.android.fingerprint.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.HardwareFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.OsBuildFingerprinter
import com.fingerprintjs.android.fingerprint.hashers.Hasher
import com.fingerprintjs.android.fingerprint.hashers.HasherType
import com.fingerprintjs.android.fingerprint.hashers.MurMur3x64x128Hasher


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
            sensorDataSource(),
            inputDevicesDataSource(),
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

    private fun createDeviceIdProvider(): DeviceIdProvider {
        return DeviceIdProviderImpl(
            gsfIdProvider(),
            androidIdProvider()
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

    private fun gsfIdProvider(): GsfIdProvider {
        return GsfIdProvider(context.contentResolver!!)
    }

    private fun androidIdProvider(): AndroidIdProvider {
        return AndroidIdProvider(context.contentResolver!!)
    }

    private fun sensorDataSource(): SensorDataSource {
        return SensorDataSourceImpl(
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        )
    }

    private fun inputDevicesDataSource(): InputDeviceDataSource {
        return InputDevicesDataSourceImpl(
            context.getSystemService(Context.INPUT_SERVICE) as InputManager
        )
    }
    //endregion
}