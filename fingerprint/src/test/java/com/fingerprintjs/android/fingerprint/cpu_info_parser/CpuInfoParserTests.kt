package com.fingerprintjs.android.fingerprint.cpu_info_parser

import com.fingerprintjs.android.fingerprint.tools.parsers.parseCpuInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class CpuInfoParserTests {

    @Test
    fun testCpuInfoParser() {
        listOf(
            CpuInfoExample1Real,
            CpuInfoExample2Real,
            CpuInfoExample3Real,
            CpuInfoExample4NotReal,
            CpuInfoExample5NotReal,
            CpuInfoExample6NotReal,
        ).forEach {
            val cpuInfo = parseCpuInfo(it.content)
            assertEquals(it.expectedCpuInfo, cpuInfo)
        }
    }
}
