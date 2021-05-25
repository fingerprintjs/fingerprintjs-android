package com.fingerprintjs.android.fingerprint.signal_providers


import org.junit.Assert.assertEquals
import org.junit.Test


class RawDataTest {
    @Test
    fun `Filtering by version is working`() {
        val ver2List = prepareSignalsList().filterByVersion(2)
        assertEquals(5, ver2List.size)
    }

    @Test
    fun `Filtering by stability level is working`() {
        val stableSignalsList = prepareSignalsList().filterByStabilityLevel(StabilityLevel.STABLE)
        val optimalSignalsList = prepareSignalsList().filterByStabilityLevel(StabilityLevel.OPTIMAL)
        val uniqueSignalsList = prepareSignalsList().filterByStabilityLevel(StabilityLevel.UNIQUE)

        assertEquals(2, stableSignalsList.size)
        assertEquals(7, optimalSignalsList.size)
        assertEquals(8, uniqueSignalsList.size)
    }
    
    private fun prepareSignalsList(): List<IdentificationSignal<*>> = listOf(
        createSignal(1, null, StabilityLevel.OPTIMAL),
        createSignal(1, 2, StabilityLevel.OPTIMAL),
        createSignal(1, 2, StabilityLevel.OPTIMAL),
        createSignal(2, null, StabilityLevel.OPTIMAL),
        createSignal(1, null, StabilityLevel.STABLE),
        createSignal(2, null, StabilityLevel.STABLE),
        createSignal(3, null, StabilityLevel.OPTIMAL),
        createSignal(1, null, StabilityLevel.UNIQUE)
    )

    private fun createSignal(
        addedInVersion: Int,
        removedInVersion: Int?,
        stabilityLevel: StabilityLevel
    ): IdentificationSignal<String> {
        return object : IdentificationSignal<String>(
            addedInVersion,
            removedInVersion,
            stabilityLevel,
            "",
            "",
            ""
        ) {
            override fun toString() = ""
        }
    }
}