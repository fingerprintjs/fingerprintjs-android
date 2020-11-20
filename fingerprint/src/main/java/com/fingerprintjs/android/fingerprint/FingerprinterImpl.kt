package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.SignalProviderType
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalProvider
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


internal class FingerprinterImpl(
    private val hardwareSignalProvider: HardwareSignalProvider,
    private val osBuildSignalProvider: OsBuildSignalProvider,
    private val deviceIdProvider: DeviceIdProvider,
    private val installedAppsSignalProvider: InstalledAppsSignalProvider,
    private val deviceStateSignalProvider: DeviceStateSignalProvider,
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
        getFingerprint(DEFAULT_MASK, listener)
    }

    override fun getFingerprint(signalProvidersMask: Int, listener: (FingerprintResult) -> Unit) {
        fingerprintResult?.let {
            listener.invoke(it)
            return
        }

        executor.execute {
            val fingerprintSb = StringBuilder()

            if (signalProvidersMask and SignalProviderType.HARDWARE != 0) {
                fingerprintSb.append(hardwareSignalProvider.fingerprint())
            }
            if (signalProvidersMask and SignalProviderType.OS_BUILD != 0) {
                fingerprintSb.append(osBuildSignalProvider.fingerprint())
            }
            if (signalProvidersMask and SignalProviderType.DEVICE_STATE != 0) {
                fingerprintSb.append(deviceStateSignalProvider.fingerprint())
            }
            if (signalProvidersMask and SignalProviderType.INSTALLED_APPS != 0) {
                fingerprintSb.append(installedAppsSignalProvider.fingerprint())
            }

            val result = object : FingerprintResult {
                override val fingerprint = configuration.hasher.hash(fingerprintSb.toString())

                @Suppress("UNCHECKED_CAST")
                override fun <T> getSignalProvider(clazz: Class<T>): T? {
                    return when (clazz) {
                        HardwareSignalProvider::class.java -> hardwareSignalProvider
                        OsBuildSignalProvider::class.java -> osBuildSignalProvider
                        DeviceStateSignalProvider::class.java -> deviceStateSignalProvider
                        InstalledAppsSignalProvider::class.java -> installedAppsSignalProvider
                        else -> null
                    } as? T
                }
            }

            listener.invoke(result)
        }
    }
}

private val DEFAULT_MASK =
    SignalProviderType.HARDWARE or SignalProviderType.OS_BUILD or SignalProviderType.DEVICE_STATE