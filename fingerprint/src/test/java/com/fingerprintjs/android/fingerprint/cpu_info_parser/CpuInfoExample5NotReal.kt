package com.fingerprintjs.android.fingerprint.cpu_info_parser

import com.fingerprintjs.android.fingerprint.info_providers.CpuInfo

// Based on real example, with some line break changes. Just to make sure that we still can parse such thing.
object CpuInfoExample5NotReal : CpuInfoExample {
    override val content = """
Processor	: AArch64 Processor rev 4 (aarch64)
processor	: 0
BogoMIPS	: 38.40
Features	: fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer	: 0x51
CPU architecture: 8
CPU variant	: 0xa
CPU part	: 0x801
CPU revision	: 4



processor	: 1
BogoMIPS	: 38.40
Features	: fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer	: 0x51
CPU architecture: 8
CPU variant	: 0xa
CPU part	: 0x801
CPU revision	: 4

processor	: 2
BogoMIPS	: 38.40
Features	: fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer	: 0x51
CPU architecture: 8
CPU variant	: 0xa
CPU part	: 0x801
CPU revision	: 4

processor	: 3
BogoMIPS	: 38.40
Features	: fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer	: 0x51
CPU architecture: 8
CPU variant	: 0xa
CPU part	: 0x801
CPU revision	: 4

processor	: 4
BogoMIPS	: 38.40
Features	: fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer	: 0x51
CPU architecture: 8
CPU variant	: 0xa
CPU part	: 0x800
CPU revision	: 2

processor	: 5
BogoMIPS	: 38.40
Features	: fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer	: 0x51
CPU architecture: 8
CPU variant	: 0xa
CPU part	: 0x800
CPU revision	: 2

processor	: 6
BogoMIPS	: 38.40
Features	: fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer	: 0x51
CPU architecture: 8
CPU variant	: 0xa
CPU part	: 0x800
CPU revision	: 2

processor	: 7
BogoMIPS	: 38.40
Features	: fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer	: 0x51
CPU architecture: 8
CPU variant	: 0xa
CPU part	: 0x800
CPU revision	: 2

Hardware	: Qualcomm Technologies, Inc SM6125 

    """.trimIndent()
    override val expectedCpuInfo = CpuInfo(
        commonInfo = listOf(
            "Processor" to "AArch64 Processor rev 4 (aarch64)",
            "Hardware" to "Qualcomm Technologies, Inc SM6125",
        ),
        perProcessorInfo = listOf(
            listOf(
                "BogoMIPS" to "38.40",
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid",
                "CPU implementer" to "0x51",
                "CPU architecture" to "8",
                "CPU variant" to "0xa",
                "CPU part" to "0x801",
                "CPU revision" to "4",
            ),
            listOf(
                "BogoMIPS" to "38.40",
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid",
                "CPU implementer" to "0x51",
                "CPU architecture" to "8",
                "CPU variant" to "0xa",
                "CPU part" to "0x801",
                "CPU revision" to "4",
            ),
            listOf(
                "BogoMIPS" to "38.40",
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid",
                "CPU implementer" to "0x51",
                "CPU architecture" to "8",
                "CPU variant" to "0xa",
                "CPU part" to "0x801",
                "CPU revision" to "4",
            ),
            listOf(
                "BogoMIPS" to "38.40",
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid",
                "CPU implementer" to "0x51",
                "CPU architecture" to "8",
                "CPU variant" to "0xa",
                "CPU part" to "0x801",
                "CPU revision" to "4",
            ),
            listOf(
                "BogoMIPS" to "38.40",
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid",
                "CPU implementer" to "0x51",
                "CPU architecture" to "8",
                "CPU variant" to "0xa",
                "CPU part" to "0x800",
                "CPU revision" to "2",
            ),
            listOf(
                "BogoMIPS" to "38.40",
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid",
                "CPU implementer" to "0x51",
                "CPU architecture" to "8",
                "CPU variant" to "0xa",
                "CPU part" to "0x800",
                "CPU revision" to "2",
            ),
            listOf(
                "BogoMIPS" to "38.40",
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid",
                "CPU implementer" to "0x51",
                "CPU architecture" to "8",
                "CPU variant" to "0xa",
                "CPU part" to "0x800",
                "CPU revision" to "2",
            ),
            listOf(
                "BogoMIPS" to "38.40",
                "Features" to "fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid",
                "CPU implementer" to "0x51",
                "CPU architecture" to "8",
                "CPU variant" to "0xa",
                "CPU part" to "0x800",
                "CPU revision" to "2",
            ),
        ),
    )
}
