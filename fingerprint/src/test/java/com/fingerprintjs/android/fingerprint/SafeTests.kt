package com.fingerprintjs.android.fingerprint

import com.fingerprintjs.android.fingerprint.tools.threading.createSharedExecutor
import com.fingerprintjs.android.fingerprint.tools.threading.runOnAnotherThread
import com.fingerprintjs.android.fingerprint.tools.threading.safe.ExecutionTimeoutException
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safe
import com.fingerprintjs.android.fingerprint.tools.threading.sharedExecutor
import junit.framework.TestCase
import org.junit.After
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutionException

class SafeTests {

    @After
    fun recreateExecutor() {
        sharedExecutor = createSharedExecutor()
    }

    @Test
    fun safeValueReturned() {
        val v = safe { 0 }
        TestCase.assertEquals(v.getOrNull(), 0)
    }

    @Test
    fun safeNestedValueReturned() {
        val v = safe { safe { 0 } }
        TestCase.assertEquals(v.getOrNull()!!.getOrNull(), 0)
    }

    @Test
    fun safeErrorRetrievable() {
        val errorId = "Hello"
        val v = safe { throw Exception(errorId) }
        val err = v.exceptionOrNull() as ExecutionException
        val errCause = err.cause!!
        TestCase.assertTrue(errCause is Exception && errCause.message == errorId)
    }

    @Test
    fun safeExecutionNeverStuck() {
        val elapsedTime = elapsedTimeMs {
            safe(timeoutMs = TimeConstants.t1) { Thread.sleep(TimeConstants.t4) }
        }
        TestCase.assertTrue(elapsedTime - TimeConstants.t1 < TimeConstants.epsilon)
    }

    @Test
    fun safeExecutionStuckThreadStackTraceReturned() {
        val res = safe(timeoutMs = TimeConstants.t1) { Thread.sleep(TimeConstants.t4) }
        val err = res.exceptionOrNull()!!
        TestCase.assertTrue(
            err is ExecutionTimeoutException
                    && err.executionThreadStackTrace != null
                    && err.executionThreadStackTrace.any { it.className == "java.lang.Thread" && it.methodName == "sleep" }
        )
    }

    @Test
    fun safeFromMultipleThreadsIsNotBlocked() {
        val countDownLatch = CountDownLatch(2)
        val elapsedTime = elapsedTimeMs {
            runOnAnotherThread { safe { Thread.sleep(TimeConstants.t1); countDownLatch.countDown() } }
            runOnAnotherThread { safe { Thread.sleep(TimeConstants.t1); countDownLatch.countDown() } }
            countDownLatch.await()
        }
        TestCase.assertTrue(elapsedTime - TimeConstants.t1 < TimeConstants.epsilon)
    }

    @Test
    fun safeThreadsAreReused() {
        for (i in 0 until 4) {
            safe { }
            TestCase.assertEquals(1, sharedExecutor.poolSize)
            Thread.sleep(TimeConstants.epsilon)
        }
    }

    // this is a sad fact but we will leave it as it is
    @Test
    fun safeThreadCountGrowsIfThreadsCantInterrupt() {
        for (i in 1 until 5) {
            safe(timeoutMs = TimeConstants.epsilon) { neverReturn() }
            TestCase.assertEquals(i, sharedExecutor.poolSize)
            Thread.sleep(TimeConstants.epsilon)
        }
    }

    @Test
    fun safeOuterTimeoutDominatesOverInner() {
        val elapsedTime = elapsedTimeMs {
            safe(timeoutMs = TimeConstants.t1) {
                safe(timeoutMs = TimeConstants.t2) {
                    Thread.sleep(TimeConstants.t3)
                }
            }
        }
        TestCase.assertTrue(elapsedTime - TimeConstants.t1 < TimeConstants.epsilon)
    }

    @Test
    fun safeNestedSafeInterrupted() {
        val errLvl1: Throwable?
        var errLvl2: Throwable? = null
        var errLvl3: Throwable? = null
        val countDownLatch = CountDownLatch(2)
        errLvl1 = safe(timeoutMs = TimeConstants.t1) {
            errLvl2 = safe(timeoutMs = TimeConstants.t2) {
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
        TestCase.assertTrue(errLvl1 is ExecutionTimeoutException)
        TestCase.assertTrue(errLvl2 is InterruptedException)
        TestCase.assertTrue(errLvl3 is InterruptedException)
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
