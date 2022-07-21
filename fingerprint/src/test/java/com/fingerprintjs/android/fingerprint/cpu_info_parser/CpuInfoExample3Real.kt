package com.fingerprintjs.android.fingerprint.cpu_info_parser

import com.fingerprintjs.android.fingerprint.info_providers.CpuInfo

object CpuInfoExample3Real : CpuInfoExample {
    override val content = """
processor       : 0
Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32
CPU implementer : 0x00
CPU architecture: 8
CPU variant     : 0x0
CPU part        : 0x000
CPU revision    : 0

processor       : 1
Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32
CPU implementer : 0x00
CPU architecture: 8
CPU variant     : 0x0
CPU part        : 0x000
CPU revision    : 0

processor       : 2
Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32
CPU implementer : 0x00
CPU architecture: 8
CPU variant     : 0x0
CPU part        : 0x000
CPU revision    : 0

processor       : 3
Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32
CPU implementer : 0x00
CPU architecture: 8
CPU variant     : 0x0
CPU part        : 0x000
CPU revision    : 0
    """.trimIndent()

    override val expectedCpuInfo = CpuInfo(
        commonInfo = emptyList(),
        perProcessorInfo = listOf(
            listOf(
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32",
                "CPU implementer" to "0x00",
                "CPU architecture" to "8",
                "CPU variant" to "0x0",
                "CPU part" to "0x000",
                "CPU revision" to "0",
            ),
            listOf(
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32",
                "CPU implementer" to "0x00",
                "CPU architecture" to "8",
                "CPU variant" to "0x0",
                "CPU part" to "0x000",
                "CPU revision" to "0",
            ),

            listOf(
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32",
                "CPU implementer" to "0x00",
                "CPU architecture" to "8",
                "CPU variant" to "0x0",
                "CPU part" to "0x000",
                "CPU revision" to "0",
            ),
            listOf(
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32",
                "CPU implementer" to "0x00",
                "CPU architecture" to "8",
                "CPU variant" to "0x0",
                "CPU part" to "0x000",
                "CPU revision" to "0",
            ),
        ),
    )
}
