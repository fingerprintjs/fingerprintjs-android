package com.fingerprintjs.android.fingerprint

import androidx.annotation.WorkerThread
import com.fingerprintjs.android.fingerprint.device_id_signals.DeviceIdSignalsProvider
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.FingerprintingSignal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.FingerprintingSignalsProvider
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.signal_providers.device_id.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.FingerprintingLegacySchemeSupportExtensions
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.threading.safe.Safe
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safe


internal class FingerprinterImpl internal constructor(
    private val legacyArgs: Fingerprinter.LegacyArgs?,
    private val fpSignalsProvider: FingerprintingSignalsProvider,
    private val deviceIdSignalsProvider: DeviceIdSignalsProvider,
) {
    @Volatile
    private var deviceIdResult: DeviceIdResult? = null
    @Volatile
    private var fingerprintResult: FingerprintResult? = null

    @WorkerThread
    @Deprecated(DeprecationMessages.DEPRECATED_SYMBOL)
    fun getDeviceId(): Result<DeviceIdResult> {
        require(legacyArgs != null)

        return safe(timeoutMs = Safe.timeoutLong) {
            deviceIdResult?.let { return@safe it }
            val deviceIdResult = DeviceIdResult(
                legacyArgs.deviceIdProvider.fingerprint(),
                legacyArgs.deviceIdProvider.rawData().gsfId().value,
                legacyArgs.deviceIdProvider.rawData().androidId().value,
                legacyArgs.deviceIdProvider.rawData().mediaDrmId().value
            )
            this.deviceIdResult = deviceIdResult
            deviceIdResult
        }
    }

    @WorkerThread
    fun getDeviceId(version: Fingerprinter.Version): Result<DeviceIdResult> {
        return safe(timeoutMs = Safe.timeoutLong) {
            DeviceIdResult(
                deviceId = deviceIdSignalsProvider.getSignalMatching(version).getIdString(),
                gsfId = deviceIdSignalsProvider.gsfIdSignal.getIdString(),
                androidId = deviceIdSignalsProvider.androidIdSignal.getIdString(),
                mediaDrmId = deviceIdSignalsProvider.mediaDrmIdSignal.getIdString(),
            )
        }
    }

    @WorkerThread
    @Deprecated(DeprecationMessages.DEPRECATED_SYMBOL)
    fun getFingerprint(
        stabilityLevel: StabilityLevel,
    ): Result<FingerprintResult> {
        require(legacyArgs != null)

        return safe(timeoutMs = Safe.timeoutLong) {
            fingerprintResult?.let { return@safe it }
            val fingerprintSb = StringBuilder()

            fingerprintSb.apply {
                append(legacyArgs.hardwareSignalProvider.fingerprint(stabilityLevel))
                append(legacyArgs.osBuildSignalProvider.fingerprint(stabilityLevel))
                append(legacyArgs.deviceStateSignalProvider.fingerprint(stabilityLevel))
                append(legacyArgs.installedAppsSignalProvider.fingerprint(stabilityLevel))
            }

            object : FingerprintResult {
                override val fingerprint = legacyArgs.configuration.hasher.hash(fingerprintSb.toString())

                @Suppress("UNCHECKED_CAST")
                override fun <T : SignalGroupProvider<*>> getSignalProvider(clazz: Class<T>): T? {
                    return when (clazz) {
                        HardwareSignalGroupProvider::class.java -> legacyArgs.hardwareSignalProvider
                        OsBuildSignalGroupProvider::class.java -> legacyArgs.osBuildSignalProvider
                        DeviceStateSignalGroupProvider::class.java -> legacyArgs.deviceStateSignalProvider
                        InstalledAppsSignalGroupProvider::class.java -> legacyArgs.installedAppsSignalProvider
                        DeviceIdProvider::class.java -> legacyArgs.deviceIdProvider
                        else -> null
                    } as? T
                }
            }
        }
    }

    @WorkerThread
    fun getFingerprint(
        version: Fingerprinter.Version,
        stabilityLevel: StabilityLevel,
        hasher: Hasher,
    ): Result<String> {
        return if (version < Fingerprinter.Version.fingerprintingFlattenedSignalsFirstVersion) {
            safe(timeoutMs = Safe.timeoutLong) {
                val joinedHashes = with(FingerprintingLegacySchemeSupportExtensions) {
                    listOf(
                        hasher.hash(fpSignalsProvider.getHardwareSignals(version, stabilityLevel)),
                        hasher.hash(fpSignalsProvider.getOsBuildSignals(version, stabilityLevel)),
                        hasher.hash(fpSignalsProvider.getDeviceStateSignals(version, stabilityLevel)),
                        hasher.hash(fpSignalsProvider.getInstalledAppsSignals(version, stabilityLevel)),
                    ).joinToString(separator = "")
                }

                hasher.hash(joinedHashes)
            }
        } else {
            getFingerprint(
                fingerprintingSignals = fpSignalsProvider.getSignalsMatching(version, stabilityLevel),
                hasher = hasher,
            )
        }
    }

    @WorkerThread
    fun getFingerprint(
        fingerprintingSignals: List<FingerprintingSignal<*>>,
        hasher: Hasher,
    ): Result<String> {
        return safe(timeoutMs = Safe.timeoutLong) { hasher.hash(fingerprintingSignals) }
    }

    internal fun getFingerprintingSignalsProvider(): FingerprintingSignalsProvider {
        return fpSignalsProvider
    }

    private fun Hasher.hash(fingerprintingSignals: List<FingerprintingSignal<*>>): String {
        val joinedString =
            fingerprintingSignals.joinToString(separator = "") { it.getHashableString() }
        return this.hash(joinedString)
    }
}
