package com.fingerprintjs.android.fingerprint.datasources


import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.io.File
import java.util.Scanner
import kotlin.collections.HashMap


interface CpuInfoProvider {
    fun cpuInfo(): Map<String, String>
}

class CpuInfoProviderImpl : CpuInfoProvider {
    override fun cpuInfo(): Map<String, String> {
        return executeSafe({getCpuInfo()}, emptyMap())
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
}

private const val CPU_INFO_PATH = "/proc/cpuinfo"
private const val KEY_VALUE_DELIMITER = ": "