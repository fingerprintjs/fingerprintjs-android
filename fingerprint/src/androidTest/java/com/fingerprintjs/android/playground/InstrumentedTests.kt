package com.fingerprintjs.android.playground

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.playground.utils.callbackToSync

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedTests {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testFpAllResultsAvailable() {
        val fingerprinter = FingerprinterFactory.getInstance(
            context = context,
            configuration = Configuration(version = 3),
        )

        val deviceId = callbackToSync<DeviceIdResult> {
            fingerprinter.getDeviceId { emit(it) }
        }
        val fingerprint = callbackToSync<FingerprintResult> {
            fingerprinter.getFingerprint { emit(it) }
        }

        Log.d(INSTRUMENTED_TESTS_TAG, """
            deviceId = ${deviceId.deviceId}
            gsfId = ${deviceId.gsfId}
            androidId = ${deviceId.androidId}
            mediaDrmId = ${deviceId.mediaDrmId}
            fingerprint = ${fingerprint.fingerprint}
        """.trimIndent())

        val checkedOutputs = listOf(
            deviceId.deviceId,
            fingerprint.fingerprint,
        )
        assert(checkedOutputs.all { it.isNotEmpty() })
    }
}

private const val INSTRUMENTED_TESTS_TAG = "INSTRUMENTED_TESTS"