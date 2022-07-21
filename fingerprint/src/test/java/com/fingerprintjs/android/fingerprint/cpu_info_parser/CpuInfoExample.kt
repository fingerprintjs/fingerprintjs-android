package com.fingerprintjs.android.fingerprint.cpu_info_parser

import com.fingerprintjs.android.fingerprint.info_providers.CpuInfo

interface CpuInfoExample {
    val content: String
    val expectedCpuInfo: CpuInfo
}
