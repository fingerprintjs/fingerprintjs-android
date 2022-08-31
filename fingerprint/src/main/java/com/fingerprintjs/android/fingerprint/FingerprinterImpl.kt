package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.signal_providers.device_id.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


internal class FingerprinterImpl(
    private val hardwareSignalProvider: HardwareSignalGroupProvider,
    private val osBuildSignalProvider: OsBuildSignalGroupProvider,
    private val deviceIdProvider: DeviceIdProvider,
    private val installedAppsSignalProvider: InstalledAppsSignalGroupProvider,
    private val deviceStateSignalProvider: DeviceStateSignalGroupProvider,
    private val configuration: Configuration
) : Fingerprinter {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    private var deviceIdResult: DeviceIdResult? = null
    private var fingerprintResult: FingerprintResult? = null

    override fun getDeviceId(listener: (DeviceIdResult) -> Unit) {
        deviceIdResult?.let {
            listener.invoke(it)
            return
        }

        executor.execute {
            val deviceIdResult = DeviceIdResult(
                deviceIdProvider.fingerprint(),
                deviceIdProvider.rawData().gsfId().value,
                deviceIdProvider.rawData().androidId().value,
                deviceIdProvider.rawData().mediaDrmId().value
            )
            this.deviceIdResult = deviceIdResult
            listener.invoke(deviceIdResult)
        }
    }

    override fun getFingerprint(
        stabilityLevel: StabilityLevel,
        listener: (FingerprintResult) -> Unit
    ) = calculateFingerprint(stabilityLevel, listener)

    private fun calculateFingerprint(
        stabilityLevel: StabilityLevel,
        listener: (FingerprintResult) -> Unit
    ) {
        // this looks like a bug
        fingerprintResult?.let {
            listener.invoke(it)
            return
        }

        executor.execute {
            val fingerprintSb = StringBuilder()

            fingerprintSb.apply {
                append(hardwareSignalProvider.fingerprint(stabilityLevel))
                append(osBuildSignalProvider.fingerprint(stabilityLevel))
                append(deviceStateSignalProvider.fingerprint(stabilityLevel))
                append(installedAppsSignalProvider.fingerprint(stabilityLevel))
            }

            val result = object : FingerprintResult {
                override val fingerprint = configuration.hasher.hash(fingerprintSb.toString())

                @Suppress("UNCHECKED_CAST")
                override fun <T : SignalGroupProvider<*>> getSignalProvider(clazz: Class<T>): T? {
                    return when (clazz) {
                        HardwareSignalGroupProvider::class.java -> hardwareSignalProvider
                        OsBuildSignalGroupProvider::class.java -> osBuildSignalProvider
                        DeviceStateSignalGroupProvider::class.java -> deviceStateSignalProvider
                        InstalledAppsSignalGroupProvider::class.java -> installedAppsSignalProvider
                        DeviceIdProvider::class.java -> deviceIdProvider
                        else -> null
                    } as? T
                }
        }

            listener.invoke(result)
        }
    }
}