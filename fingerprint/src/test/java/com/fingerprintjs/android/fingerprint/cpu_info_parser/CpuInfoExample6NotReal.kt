package com.fingerprintjs.android.fingerprint.cpu_info_parser

import com.fingerprintjs.android.fingerprint.info_providers.CpuInfo

object CpuInfoExample6NotReal : CpuInfoExample {
    override val content = """
        
    """.trimIndent()
    override val expectedCpuInfo = CpuInfo.EMPTY
}
