package com.fingerprintjs.android.fingerprint

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.FingerprintingLegacySchemeSupportExtensions.getDeviceStateSignals
import com.fingerprintjs.android.fingerprint.tools.FingerprintingLegacySchemeSupportExtensions.getHardwareSignals
import com.fingerprintjs.android.fingerprint.tools.FingerprintingLegacySchemeSupportExtensions.getInstalledAppsSignals
import com.fingerprintjs.android.fingerprint.tools.FingerprintingLegacySchemeSupportExtensions.getOsBuildSignals
import com.fingerprintjs.android.fingerprint.tools.threading.safe.Safe
import com.fingerprintjs.android.fingerprint.utils.callbackToSync
import io.mockk.every
import io.mockk.mockkObject
import junit.framework.TestCase
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedTests {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testDeviceIdAvailable() {
        val fingerprinter = FingerprinterFactory.create(context)
        for (version in Fingerprinter.Version.values()) {
            val deviceId = callbackToSync { fingerprinter.getDeviceId(version) { emit(it) } }
            assert(deviceId.deviceId.isNotEmpty())
        }
    }

    @Test
    fun testFingerprintAvailable() {
        val fingerprinter = FingerprinterFactory.create(context)
        for (version in Fingerprinter.Version.values()) {
            for (stabilityLevel in StabilityLevel.values()) {
                val fingerprint = callbackToSync {
                    fingerprinter.getFingerprint(
                        version = version,
                        stabilityLevel = stabilityLevel,
                    ) { emit(it) }
                }
                assert(fingerprint !in listOf("", "00")) // 00 is a hash of empty string, copied from hash tests
            }
        }
    }

    private fun assertFlaky(
        tolerance: Int,
        block: () -> Unit,
    ) {
        for (i in 0 until tolerance) {
            try {
                block.invoke()
                return
            } catch (_: AssertionError) {
            }
        }
        block.invoke()
    }

    @Test
    fun testFingerprintApiBackwardCompatibility() {
        Fingerprinter.Version.values()
            .takeWhile { it != Fingerprinter.Version.fingerprintingFlattenedSignalsFirstVersion }
            .forEach { version ->
                StabilityLevel.values().forEach { stabilityLevel ->
                    assertFlaky(tolerance = 5) {
                        val fingerprinter = FingerprinterFactory.getInstance(
                            context = context,
                            configuration = Configuration(
                                version = version.intValue,
                            )
                        )

                        val fingerprintLegacy = callbackToSync {
                            fingerprinter.getFingerprint(stabilityLevel) { emit(it) }
                        }
                        val fingerprint = callbackToSync {
                            fingerprinter.getFingerprint(
                                version = version,
                                stabilityLevel = stabilityLevel
                            ) { emit(it) }
                        }

                        assertEquals(fingerprintLegacy.fingerprint, fingerprint)
                    }
                }
            }
    }

    @Test
    fun testDeviceIdApiBackwardCompatibility() {
        Fingerprinter.Version.values()
            .takeWhile { it != Fingerprinter.Version.fingerprintingFlattenedSignalsFirstVersion }
            .forEach { version ->
                val fingerprinter = FingerprinterFactory.getInstance(
                    context = context,
                    configuration = Configuration(
                        version = version.intValue,
                    )
                )

                val deviceIdLegacy = callbackToSync {
                    fingerprinter.getDeviceId { emit(it) }
                }
                val deviceId = callbackToSync {
                    fingerprinter.getDeviceId(version = version) { emit(it) }
                }

                assertEquals(deviceIdLegacy, deviceId)
            }
    }

    @Test
    fun testIllegalLegacyApiUsage() {
        runCatching {
            FingerprinterFactory.getInstance(
                context,
                Configuration(
                    version = Fingerprinter.Version.fingerprintingFlattenedSignalsFirstVersion.intValue,
                )
            )
        }.run { assertTrue(isFailure) }

        val fp = FingerprinterFactory.create(context)
        runCatching { fp.getFingerprint { } }.run { assertTrue(isFailure) }
        runCatching { fp.getDeviceId { } }.run { assertTrue(isFailure) }
    }

    @Test
    fun testCustomFingerprintIdenticalToDefault() {
        val fingerprinter = FingerprinterFactory.create(context)
        Fingerprinter.Version.values()
            .forEach { version ->
                StabilityLevel.values().forEach { stabilityLevel ->
                    val fp1 = callbackToSync {
                        fingerprinter.getFingerprint(
                            version = version,
                            stabilityLevel = stabilityLevel
                        ) { emit(it) }
                    }
                    val fp2 = fingerprinter.getFingerprint(
                        fingerprintingSignals = fingerprinter.getFingerprintingSignalsProvider()
                            ?.getSignalsMatching(
                                version = version,
                                stabilityLevel = stabilityLevel
                            ).orEmpty()
                    )
                    if (version >= Fingerprinter.Version.fingerprintingFlattenedSignalsFirstVersion) {
                        assertEquals(fp1, fp2)
                    } else {
                        assertNotEquals(fp1, fp2)
                    }
                }
            }
    }

    @Test
    fun testFingerprintingSignalsProviderReturnsCorrectMatchingSignalsForLegacyVersions() {
        val fingerprintingSignalsProvider = FingerprinterFactory.create(context).getFingerprintingSignalsProvider()
        Fingerprinter.Version.values().takeWhile { it <= Fingerprinter.Version.fingerprintingGroupedSignalsLastVersion }
            .forEach { version ->
                StabilityLevel.values().forEach { stabilityLevel ->
                    val expectedLegacySignalsInfos = listOf(
                        fingerprintingSignalsProvider?.getDeviceStateSignals(version, stabilityLevel).orEmpty(),
                        fingerprintingSignalsProvider?.getHardwareSignals(version, stabilityLevel).orEmpty(),
                        fingerprintingSignalsProvider?.getOsBuildSignals(version, stabilityLevel).orEmpty(),
                        fingerprintingSignalsProvider?.getInstalledAppsSignals(version, stabilityLevel).orEmpty(),
                    )
                        .flatten()
                        .map { it.info }
                        .toSet()
                    val matchingSignalsInfos = fingerprintingSignalsProvider
                        ?.getSignalsMatching(version, stabilityLevel)
                        .orEmpty()
                        .map { it.info }
                        .toSet()
                    assertEquals(expectedLegacySignalsInfos, matchingSignalsInfos)
                }
            }
    }

    @Test
    fun nestedSafeCallNeverHappens() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P)
            return // object mocks are not supported

        var logCalled = false
        mockkObject(Safe)
        every { Safe.logIllegalSafeWithTimeoutUsage() } answers { logCalled = true }

        Fingerprinter.Version.values().forEach { version ->
            val fingerprinter = FingerprinterFactory.create(context)
            val deviceId = callbackToSync { fingerprinter.getDeviceId(version = version) { emit(it) } }
            StabilityLevel.values().forEach { stabilityLevel ->
                val fingerprint = callbackToSync { fingerprinter.getFingerprint(version, stabilityLevel) { emit(it) } }
            }
            val fingerprintingSignalsProvider = fingerprinter.getFingerprintingSignalsProvider()!!
        }

        TestCase.assertEquals(false, logCalled)
    }
}
