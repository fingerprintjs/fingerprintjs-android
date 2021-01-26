package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProviderType
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
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
                deviceIdProvider.getDeviceId(),
                deviceIdProvider.getGsfId(),
                deviceIdProvider.getAndroidId()
            )
            this.deviceIdResult = deviceIdResult
            listener.invoke(deviceIdResult)
        }
    }

    override fun getFingerprint(listener: (FingerprintResult) -> Unit) {
        getFingerprint(StabilityLevel.OPTIMAL, ALL_PROVIDERS_MASK, listener)
    }

    override fun getFingerprint(
        stabilityLevel: StabilityLevel,
        listener: (FingerprintResult) -> Unit
    ) = getFingerprint(stabilityLevel, ALL_PROVIDERS_MASK, listener)

    override fun getFingerprint(signalProvidersMask: Int, listener: (FingerprintResult) -> Unit) =
        getFingerprint(StabilityLevel.UNIQUE, signalProvidersMask, listener)

    private fun getFingerprint(
        stabilityLevel: StabilityLevel,
        signalProvidersMask: Int,
        listener: (FingerprintResult) -> Unit
    ) {
        fingerprintResult?.let {
            listener.invoke(it)
            return
        }

        executor.execute {
            val fingerprintSb = StringBuilder()

            if (signalProvidersMask and SignalGroupProviderType.HARDWARE != 0) {
                fingerprintSb.append(hardwareSignalProvider.fingerprint(stabilityLevel))
            }
            if (signalProvidersMask and SignalGroupProviderType.OS_BUILD != 0) {
                fingerprintSb.append(osBuildSignalProvider.fingerprint(stabilityLevel))
            }
            if (signalProvidersMask and SignalGroupProviderType.DEVICE_STATE != 0) {
                fingerprintSb.append(deviceStateSignalProvider.fingerprint(stabilityLevel))
            }
            if (signalProvidersMask and SignalGroupProviderType.INSTALLED_APPS != 0) {
                fingerprintSb.append(installedAppsSignalProvider.fingerprint(stabilityLevel))
            }

            val result = object : FingerprintResult {
                override val fingerprint = configuration.hasher.hash(fingerprintSb.toString())

                @Suppress("UNCHECKED_CAST")
                override fun <T> getSignalProvider(clazz: Class<T>): T? {
                    return when (clazz) {
                        HardwareSignalGroupProvider::class.java -> hardwareSignalProvider
                        OsBuildSignalGroupProvider::class.java -> osBuildSignalProvider
                        DeviceStateSignalGroupProvider::class.java -> deviceStateSignalProvider
                        InstalledAppsSignalGroupProvider::class.java -> installedAppsSignalProvider
                        else -> null
                    } as? T
                }
            }

            listener.invoke(result)
        }
    }
}

private val ALL_PROVIDERS_MASK =
    SignalGroupProviderType.HARDWARE or SignalGroupProviderType.OS_BUILD or SignalGroupProviderType.DEVICE_STATE or SignalGroupProviderType.INSTALLED_APPS

