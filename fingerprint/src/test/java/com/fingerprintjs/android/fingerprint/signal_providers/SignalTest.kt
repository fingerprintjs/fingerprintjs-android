package com.fingerprintjs.android.fingerprint.signal_providers

import com.fingerprintjs.android.fingerprint.info_providers.CpuInfo
import org.junit.Test
import org.junit.Assert.assertEquals

class SignalTest {
    @Test
    fun cpuInfoToMapTest() {
        val cpuInfo = CpuInfo(
            commonInfo = listOf(
                "Processor" to "Some processor",
                "Hardware" to "Some hardware",
            ),
            perProcessorInfo = listOf(
                listOf(
                    "some constant prop 0" to "0",
                    "some constant prop 1" to "1",
                    "some constant prop 2" to "2",
                    "some changing prop" to "0",
                ),
                listOf(
                    "some constant prop 0" to "0",
                    "some constant prop 1" to "1",
                    "some constant prop 2" to "2",
                    "some changing prop" to "0",
                ),
                listOf(
                    "some constant prop 0" to "0",
                    "some constant prop 1" to "1",
                    "some constant prop 2" to "2",
                    "some changing prop" to "1",
                ),
            ),
        )

        val expectedMap = mapOf(
            "v" to mapOf(
                "commonProps" to listOf(
                    "Processor" to "Some processor",
                    "Hardware" to "Some hardware",
                ),
                "repeatedProps" to listOf(
                    "some constant prop 0" to "0",
                    "some constant prop 1" to "1",
                    "some constant prop 2" to "2",
                ),
                "uniquePerCpuProps" to listOf(
                    listOf(
                        "some changing prop" to "0",
                    ),
                    listOf(
                        "some changing prop" to "0",
                    ),
                    listOf(
                        "some changing prop" to "1",
                    ),
                )
            )
        )

        val signal = object : IdentificationSignal<CpuInfo>(
            addedInVersion = 1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
            name = "test",
            displayName = "test",
            value = cpuInfo
        ) {
            override fun toString(): String {
                return "test"
            }
        }

        assertEquals(expectedMap, signal.toMap())
    }
}
