package com.fingerprintjs.android.fingerprint

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.threading.createSharedExecutor
import com.fingerprintjs.android.fingerprint.tools.threading.runOnAnotherThread
import com.fingerprintjs.android.fingerprint.tools.threading.safe.ExecutionTimeoutException
import com.fingerprintjs.android.fingerprint.tools.threading.safe.Safe
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safeWithTimeout
import com.fingerprintjs.android.fingerprint.tools.threading.sharedExecutor
import com.fingerprintjs.android.fingerprint.utils.callbackToSync
import com.fingerprintjs.android.fingerprint.utils.mockkObjectSupported
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import io.mockk.verifyOrder
import junit.framework.TestCase
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutionException

@RunWith(AndroidJUnit4::class)
class SafeTests {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @After
    fun recreateExecutor() {
        sharedExecutor = createSharedExecutor()
    }

    @Test
    fun safeWithTimeoutValueReturned() {
        val v = safeWithTimeout { 0 }
        TestCase.assertEquals(v.getOrNull(), 0)
    }

    @Test
    fun safeWithTimeoutErrorRetrievable() {
        val errorId = "Hello"
        val v = safeWithTimeout { throw Exception(errorId) }
        val err = v.exceptionOrNull() as ExecutionException
        val errCause = err.cause!!
        TestCase.assertTrue(errCause is Exception && errCause.message == errorId)
    }

    @Test
    fun safeWithTimeoutExecutionNeverStuck() {
        val elapsedTime = elapsedTimeMs {
            safeWithTimeout(timeoutMs = TimeConstants.t1) { Thread.sleep(TimeConstants.t4) }
        }
        TestCase.assertTrue(elapsedTime - TimeConstants.t1 < TimeConstants.epsilon)
    }

    @Test
    fun safeWithTimeoutExecutionStuckThreadStackTraceReturned() {
        val res = safeWithTimeout(timeoutMs = TimeConstants.t1) { Thread.sleep(TimeConstants.t4) }
        val err = res.exceptionOrNull()!!
        TestCase.assertTrue(
            err is ExecutionTimeoutException
                    && err.executionThreadStackTrace != null
                    && err.executionThreadStackTrace.any { it.className == "java.lang.Thread" && it.methodName == "sleep" }
        )
    }

    @Test
    fun safeWithTimeoutFromMultipleThreadsIsNotBlocked() {
        val countDownLatch = CountDownLatch(2)
        val elapsedTime = elapsedTimeMs {
            runOnAnotherThread { safeWithTimeout { Thread.sleep(TimeConstants.t1); countDownLatch.countDown() } }
            runOnAnotherThread { safeWithTimeout { Thread.sleep(TimeConstants.t1); countDownLatch.countDown() } }
            countDownLatch.await()
        }
        TestCase.assertTrue(elapsedTime - TimeConstants.t1 < TimeConstants.epsilon)
    }

    @Test
    fun safeWithTimeoutThreadsAreReused() {
        for (i in 0 until 4) {
            safeWithTimeout { }
            TestCase.assertEquals(1, sharedExecutor.poolSize)
            Thread.sleep(TimeConstants.epsilon)
        }
    }

    // this is a sad fact but we will leave it as it is
    @Test
    fun safeWithTimeoutThreadCountGrowsIfThreadsCantInterrupt() {
        for (i in 1 until 5) {
            safeWithTimeout(timeoutMs = TimeConstants.epsilon) { neverReturn() }
            TestCase.assertEquals(i, sharedExecutor.poolSize)
            Thread.sleep(TimeConstants.epsilon)
        }
    }

    @Test
    fun safeWithTimeoutOuterTimeoutDominatesOverInner() {
        val elapsedTime = elapsedTimeMs {
            safeWithTimeout(timeoutMs = TimeConstants.t1) {
                safeWithTimeout(timeoutMs = TimeConstants.t2) {
                    Thread.sleep(TimeConstants.t3)
                }
            }
        }
        TestCase.assertTrue(elapsedTime - TimeConstants.t1 < TimeConstants.epsilon)
    }

    /**
     * This test illustrates the behaviour when using one safe call inside the another.
     * Such usage is prohibited, but we'd rather know the what-ifs.
     */
    @Test
    fun safeWithTimeoutNestedSafeInterruptedBehaviour() {
        if (!mockkObjectSupported()) return
        val errLvl1: Throwable?
        var errLvl2: Throwable? = null
        var errLvl3: Throwable? = null
        val countDownLatch = CountDownLatch(2)
        mockkObject(Safe)
        every { Safe.logIllegalSafeWithTimeoutUsage() } answers {}

        errLvl1 = safeWithTimeout(timeoutMs = TimeConstants.t1) {
            errLvl2 = safeWithTimeout(timeoutMs = TimeConstants.t2) {
                try {
                    Thread.sleep(TimeConstants.t3)
                } catch (t: Throwable) {
                    errLvl3 = t
                    countDownLatch.countDown()
                }
            }.exceptionOrNull()
            countDownLatch.countDown()
        }.exceptionOrNull()
        countDownLatch.await()

        unmockkObject(Safe)
        TestCase.assertTrue(errLvl1 is ExecutionTimeoutException)
        TestCase.assertTrue(errLvl2 is InterruptedException)
        TestCase.assertTrue(errLvl3 is InterruptedException)
    }


    /**
     * Same motivation for the test as for the above.
     */
    @Test
    fun safeWithTimeoutNestedValueReturned() {
        if (!mockkObjectSupported()) return
        mockkObject(Safe)
        every { Safe.logIllegalSafeWithTimeoutUsage() } answers { }

        val v = safeWithTimeout { safeWithTimeout { 0 } }

        unmockkObject(Safe)
        TestCase.assertEquals(v.getOrNull()!!.getOrNull(), 0)
    }

    @Test
    fun safeContextFlagUnsetWhenSafeBlockReturns() =
        safeWithTimeoutContextFlagUnset(whenBlockThrows = false)

    @Test
    fun safeContextFlagUnsetWhenSafeBlockThrows() =
        safeWithTimeoutContextFlagUnset(whenBlockThrows = true)

    private fun safeWithTimeoutContextFlagUnset(whenBlockThrows: Boolean) {
        if (!mockkObjectSupported()) return
        mockkObject(Safe)
        var clearThreadId: Long? = null
        every { Safe.clearInsideSafeWithTimeout() } answers {
            callOriginal().also { clearThreadId = Thread.currentThread().id }
        }
        var markThreadId: Long? = null
        every { Safe.markInsideSafeWithTimeout() } answers {
            callOriginal().also { markThreadId = Thread.currentThread().id }
        }

        safeWithTimeout {
            if (whenBlockThrows)
                throw Exception()
        }

        verify(exactly = 1) {
            Safe.markInsideSafeWithTimeout()
            Safe.clearInsideSafeWithTimeout()
        }
        verifyOrder {
            Safe.markInsideSafeWithTimeout()
            Safe.clearInsideSafeWithTimeout()
        }

        TestCase.assertEquals(markThreadId, clearThreadId)
        unmockkObject(Safe)
    }

    @Test
    fun safeWithTimeoutNestedUsageReported() {
        if (!mockkObjectSupported()) return
        var logCalled = false
        mockkObject(Safe)
        every { Safe.logIllegalSafeWithTimeoutUsage() } answers { logCalled = true }

        safeWithTimeout { safeWithTimeout {} }

        unmockkObject(Safe)
        TestCase.assertEquals(true, logCalled)
    }


    @Test
    fun nestedSafeCallNeverHappens() {
        if (!mockkObjectSupported()) return

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

private object TimeConstants {
    const val epsilon = 200L
    const val t1 = epsilon * 3
    const val t2 = t1 * 2
    const val t3 = t1 * 3
    const val t4 = t1 * 4
}

private inline fun elapsedTimeMs(block: () -> Unit): Long {
    val currentTime = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - currentTime
}

@Suppress("ControlFlowWithEmptyBody")
private fun neverReturn() {
    while (true);
}
