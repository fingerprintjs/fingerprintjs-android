package com.fingerprintjs.android.fingerprint


import android.app.ActivityManager
import android.content.Context
import android.os.Environment
import android.os.StatFs
import com.fingerprintjs.android.fingerprint.datasources.*
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

    private var context: Context? = null

    @JvmStatic
    fun getInitializedInstance(
        context: Context,
        configuration: FingerprintAndroidConfiguration = defaultConfiguration()
    ): FingerprintAndroidAgent {
        instance?.let {
            return it
        }

        this.configuration = configuration
        this.context = context
        this.instance = FingerprintAndroidAgentImpl(
            createHardwareFingerprinter(),
            createOsBuildInfoFingerprinter(),
            createDeviceIdProvider(),
            getHasherWithType()
        )
        return instance!!
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
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val internalStorageStatFs = StatFs(Environment.getRootDirectory().absolutePath)
        val externalStorageStatFs = StatFs(context?.getExternalFilesDir(null)?.absolutePath!!)
        return MemInfoProviderImpl(activityManager, internalStorageStatFs, externalStorageStatFs)
    }

    private fun createOsBuildInfoProvider(): OsBuildInfoProvider {
        return OsBuildInfoProviderImpl()
    }

    private fun gsfIdProvider(): GsfIdProvider {
        return GsfIdProvider(context?.contentResolver!!)
    }

    private fun androidIdProvider(): AndroidIdProvider {
        return AndroidIdProvider(context?.contentResolver!!)
    }
    //endregion
}