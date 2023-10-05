package com.fingerprintjs.android.fingerprint

import com.fingerprintjs.android.fingerprint.tools.flatten
import junit.framework.TestCase
import org.junit.Test

class ResultExtensionsTests {

    @Test
    fun flattenOuterErrorReturned() {
        val res = runCatching { runCatching { 0 }.also { throw Exception() } }.flatten()
        TestCase.assertTrue(res.isFailure)
        TestCase.assertTrue(res.exceptionOrNull() is Exception)
    }

    @Test
    fun flattenInnerValueReturned() {
        val res = runCatching { runCatching { 0 } }.flatten()
        TestCase.assertTrue(res.isSuccess)
        TestCase.assertEquals(0, res.getOrThrow())
    }

    @Test
    fun flattenInnerErrorReturned() {
        val res = runCatching { runCatching { 0.also { throw Exception() } } }.flatten()
        TestCase.assertTrue(res.isFailure)
        TestCase.assertTrue(res.exceptionOrNull() is Exception)
    }
}