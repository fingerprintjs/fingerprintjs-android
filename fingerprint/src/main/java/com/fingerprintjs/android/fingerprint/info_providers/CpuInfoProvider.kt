package com.fingerprintjs.android.fingerprint.info_providers


import android.os.Build
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import com.fingerprintjs.android.fingerprint.tools.parsers.parseCpuInfo
import java.io.File
import java.util.Scanner


data class CpuInfo(
    val commonInfo: List<Pair<String, String>>,
    // except processor : x pairs. index in list may be considered as an index of a processor.
    val perProcessorInfo: List<List<Pair<String, String>>>,
) {
    companion object {
        val EMPTY = CpuInfo(
            commonInfo = emptyList(),
            perProcessorInfo = emptyList(),
        )
    }
}

interface CpuInfoProvider {
    fun cpuInfo(): Map<String, String>
    fun cpuInfoV2(): CpuInfo
    fun abiType(): String
    fun coresCount(): Int
}

internal class CpuInfoProviderImpl :
    CpuInfoProvider {
    override fun cpuInfo(): Map<String, String> {
        return executeSafe({ getCpuInfo() }, emptyMap())
    }

    override fun cpuInfoV2(): CpuInfo {
        return executeSafe(
            {
                getCpuInfoV2()
            },
            CpuInfo.EMPTY
        )
    }

    @Suppress("DEPRECATION")
    override fun abiType(): String {
        return executeSafe({
            if (Build.VERSION.SDK_INT >= 21) {
                Build.SUPPORTED_ABIS[0]
            } else {
                Build.CPU_ABI
            }
        }, "")
    }

    override fun coresCount(): Int {
        return executeSafe({
            Runtime.getRuntime().availableProcessors()
        }, 0)
    }

    private fun getCpuInfo(): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        val s = Scanner(File(CPU_INFO_PATH))
        while (s.hasNextLine()) {
            val cpuInfoValues = s.nextLine().split(KEY_VALUE_DELIMITER)
            if (cpuInfoValues.size > 1) map[cpuInfoValues[0].trim { it <= ' ' }] =
                cpuInfoValues[1].trim { it <= ' ' }
        }

        return map
    }

    private fun getCpuInfoV2(): CpuInfo {
        val cpuInfoContents = File(CPU_INFO_PATH).readText()
        return parseCpuInfo(cpuInfoContents)
    }
}

private const val CPU_INFO_PATH = "/proc/cpuinfo"
private const val KEY_VALUE_DELIMITER = ": "
