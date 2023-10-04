package com.fingerprintjs.android.fingerprint

import androidx.annotation.Discouraged
import androidx.annotation.WorkerThread
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
import com.fingerprintjs.android.fingerprint.tools.DummyResults
import com.fingerprintjs.android.fingerprint.tools.flatten
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher
import com.fingerprintjs.android.fingerprint.tools.logs.Logger
import com.fingerprintjs.android.fingerprint.tools.logs.ePleaseReport
import com.fingerprintjs.android.fingerprint.tools.threading.runOnAnotherThread
import com.fingerprintjs.android.fingerprint.tools.threading.safe.Safe
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safe


public class Fingerprinter internal constructor(
    private val implFactory: () -> FingerprinterImpl,
    private val isLegacyFactory: Boolean,
) {
    private val impl by lazy {
        // we use long timeout here, so that any single hiccup during the initialization
        // does not ruin an entire operation.
        // another option could be to not use timeout at all, since we have a lot of timeouts
        // deep inside.
        safe(timeoutMs = Safe.timeoutLong) { implFactory() }
    }

    /**
     * Retrieve the device ID information.
     *
     * Subsequent calls will return cached results.
     *
     * This method and the [Configuration] class are now deprecated in favor of the getDeviceId(version, listener) method
     * which gives an ability to get a device ID with different parameters without reinstantiation of the whole [Fingerprinter] class.
     *
     * @param listener device ID listener.
     * @throws IllegalStateException if [Fingerprinter] was retrieved via [FingerprinterFactory.create] method.
     */
    @Deprecated(message = """
        This method has been deprecated in favor of getDeviceId(version, listener) overload. Check out method doc for details.
    """)
    @Throws(IllegalStateException::class)
    public fun getDeviceId(listener: (DeviceIdResult) -> Unit) {
        checkThisLegacyMethodSupported()

        runFingerprinterImplOnAnotherThread(
            onError = {
                listener.invoke(DummyResults.deviceIdResult)
                Logger.ePleaseReport(it)
            },
            onSuccess = listener,
        ) { getDeviceId() }
    }

    /**
     * Retrieve the device ID information.
     *
     * Device IDs are cached internally, so it makes no sense to call this method more than once with the same [version].
     * If you want to re-evaluate a device ID value, you should reinitialize [Fingerprinter] using the
     * [FingerprinterFactory.create] method.
     *
     * @param version identification version. Check out [Version] for details.
     * @param listener device ID listener.
     */
    public fun getDeviceId(version: Version, listener: (DeviceIdResult) -> Unit) {
        runFingerprinterImplOnAnotherThread(
            onError = {
                listener.invoke(DummyResults.deviceIdResult)
                Logger.ePleaseReport(it)
            },
            onSuccess = listener,
        ) { getDeviceId(version) }
    }

    /**
     * Retrieve the device fingerprint information.

     * This method and the [Configuration] class are now deprecated in favor of getFingerprint(version,stabilityLevel, hasher, listener)
     * which gives an ability to get fingerprints with different parameters without reinstantiation of the whole [Fingerprinter] class.
     * Check the [FingerprintResult] documentation on how it's properties can be replaced with the newer API.
     *
     * Fingerprinting signals are cached internally, so it makes no sense to call this method more than once with the same [stabilityLevel].
     *
     * @param stabilityLevel stability level. Check out [StabilityLevel] for details.
     * @param listener listener for [FingerprintResult].
     * @throws IllegalStateException if [Fingerprinter] was retrieved via the [FingerprinterFactory.create] method.
     */
    @Deprecated("""
        This method is deprecated in favor of getFingerprint(version,stabilityLevel, hasher, listener)
        overload. Check out method doc for details.
        """)
    @Throws(IllegalStateException::class)
    @JvmOverloads
    public fun getFingerprint(
        stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL,
        listener: (FingerprintResult) -> Unit
    ) {
        checkThisLegacyMethodSupported()

        runFingerprinterImplOnAnotherThread(
            onError = {
                listener.invoke(DummyResults.fingerprintResult)
                Logger.ePleaseReport(it)
            },
            onSuccess = listener,
        ) { getFingerprint(stabilityLevel) }
    }

    /**
     * Retrieve the device fingerprint information. This is the most convenient method to get device fingerprints.
     *
     * If you want to re-evaluate fingerprint, you should reinitialize [Fingerprinter] using the
     * [FingerprinterFactory.create] method.
     *
     * Signals for fingerprinting are cached internally, so it makes no sense to call this method more than once with the same parameters.
     *
     * @param version identification version. Check out [Version] for details.
     * @param stabilityLevel stability level. Check out [StabilityLevel] for details.
     * @param hasher hash implementation. Check out [Hasher] for details.
     * @param listener listener for device fingerprint.
     */
    @JvmOverloads
    public fun getFingerprint(
        version: Version,
        stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL,
        hasher: Hasher = MurMur3x64x128Hasher(),
        listener: (String) -> (Unit),
    ) {
        runFingerprinterImplOnAnotherThread(
            onError = {
                listener.invoke(DummyResults.fingerprint)
                Logger.ePleaseReport(it)
            },
            onSuccess = listener,
        ) { getFingerprint(version, stabilityLevel, hasher) }
    }

    /**
     * A method for retrieving a custom fingerprint using only provided [fingerprintingSignals].
     * The signals are supposed to be retrieved using [FingerprintingSignalsProvider], which is retrieved
     * via [getFingerprintingSignalsProvider].
     *
     * Note that if you use this method (along with [FingerprintingSignalsProvider's][FingerprintingSignalsProvider]
     * synchronous methods), you should take responsibility for moving the work into a background thread to avoid potential
     * frame drops or ANRs.
     *
     * @param fingerprintingSignals signals used to retrieve device fingerprint.
     * @param hasher hash implementation. Check out [Hasher] for details.
     * @return Device fingerpint.
     */
    @WorkerThread
    @JvmOverloads
    public fun getFingerprint(
        fingerprintingSignals: List<FingerprintingSignal<*>>,
        hasher: Hasher = MurMur3x64x128Hasher(),
    ): String {
        return impl
            .map { it.getFingerprint(fingerprintingSignals, hasher) }
            .flatten()
            .onFailure { Logger.ePleaseReport(it) }
            .getOrDefault(DummyResults.fingerprint)
    }

    /**
     * @return [FingerprintingSignalsProvider] which is useful in conjunction with the getFingerprint(signals, hasher) method, or null
     * if some unexpected error occurred.
     */
    @WorkerThread
    public fun getFingerprintingSignalsProvider(): FingerprintingSignalsProvider? {
        return impl
            .map { it.getFingerprintingSignalsProvider() }
            .onFailure { Logger.ePleaseReport(it) }
            .getOrNull()
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun checkThisLegacyMethodSupported() {
        if (!isLegacyFactory) {
            throw (IllegalStateException(
                "To call this deprecated method, the instance " +
                        "must be retrieved using deprecated factory method."
            ))
        }
    }

    private inline fun <T> runFingerprinterImplOnAnotherThread(
        crossinline onError: (Throwable) -> Unit,
        crossinline onSuccess: (T) -> Unit,
        crossinline call: FingerprinterImpl.() -> Result<T>,
    ) {
        runOnAnotherThread {
            impl.map { it.call() }.flatten().fold(
                onSuccess = { onSuccess.invoke(it) },
                onFailure = onError,
            )
        }.onFailure(onError)
    }

    /**
     * This class represents the version of the logic provided by the [Fingerprinter] API.
     * Whenever we implement new signals (completely new or just more stable variants of existing ones)
     * for device ID or fingerprinting, the version is incremented.
     *
     * Note that changing [Version] leads to changing device id
     * and/or fingerprint returned by the [Fingerprinter] API.
     */
    public enum class Version(
        internal val intValue: Int
    ) {
        V_1(intValue = 1),
        V_2(intValue = 2),
        V_3(intValue = 3),
        V_4(intValue = 4),
        V_5(intValue = 5);

        public companion object {
            @get:Discouraged(
                message = "Use this value with a great caution. Since it will change over time " +
                        "with the library updates, using it as a parameter to the library API may lead " +
                        "to unintended change of the results of this API."
            )
            public val latest: Version
                get() = values().last()

            internal val fingerprintingFlattenedSignalsFirstVersion: Version
                get() = V_5

            internal val fingerprintingGroupedSignalsLastVersion: Version
                get() = V_4
        }
    }

    internal data class LegacyArgs(
        val hardwareSignalProvider: HardwareSignalGroupProvider,
        val osBuildSignalProvider: OsBuildSignalGroupProvider,
        val deviceIdProvider: DeviceIdProvider,
        val installedAppsSignalProvider: InstalledAppsSignalGroupProvider,
        val deviceStateSignalProvider: DeviceStateSignalGroupProvider,
        val configuration: Configuration
    )
}

/**
 * Represents the device fingerprint information.
 *
 * This class is deprecated along with the [Fingerprinter's][Fingerprinter] getFingerprint(stabilityLevel, listener) method.
 * Rules for migrating to newer APIs are listed below.
 *
 * If you relied only on the [fingerprint] property, you can retrieve it using the new
 * getFingerprint(version, stabilityLevel, hasher, listener) method.
 *
 * If you relied on [SignalGroupProvider.rawData] to retrieve any specific signals, you can do it
 * using [FingerprintingSignalsProvider]. The [FingerprintingSignalsProvider] class makes it easier, because it represents all the
 * signals in one place, so there is no more need to access different signals via different [SignalGroupProvider]s.
 *
 * Lastly, if you relied on [SignalGroupProvider.fingerprint] which essentially returned a fingerprint of a whole
 * *group* of signals, then there is no any one-line alternative in the newer API, because we intentionally removed the
 * *grouping* of signals and made signal's hierarchy flat. That means that now you will have to pick a set of signals by yourself
 * via [FingerprintingSignalsProvider] and pass them to the [Fingerprinter]'s getFingerprint(fingerprintingSignals, hasher) method.
 */
@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public interface FingerprintResult {
    public val fingerprint: String
    public fun <T : SignalGroupProvider<*>> getSignalProvider(clazz: Class<T>): T?
}

/**
 * Represents the device ID information.
 * @property deviceId This is the one of the IDs listed below – the one is considered the most appropriate
 * as a default device identifier.
 * @property gsfId GSF ID.
 * @property androidId Android ID.
 * @property mediaDrmId Media DRM ID.
 */
public data class DeviceIdResult(
    val deviceId: String,
    val gsfId: String,
    val androidId: String,
    val mediaDrmId: String,
)
