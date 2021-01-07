package com.fingerprintjs.android.fingerprint.info_providers


import android.os.Build
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.io.File
import java.util.Scanner


interface CpuInfoProvider {
    fun cpuInfo(): Map<String, String>
    fun abiType(): String
    fun coresCount(): Int
}

class CpuInfoProviderImpl :
    CpuInfoProvider {
    override fun cpuInfo(): Map<String, String> {
        return executeSafe({ getCpuInfo() }, emptyMap())
    }

    @Suppress("DEPRECATION")
    override fun abiType(): String {
        return if (Build.VERSION.SDK_INT >= 21) {
            Build.SUPPORTED_ABIS[0]
        } else {
            Build.CPU_ABI
        }
    }

    override fun coresCount(): Int {
        return if (Build.VERSION.SDK_INT >= 17) {
            Runtime.getRuntime().availableProcessors()
        } else 0
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