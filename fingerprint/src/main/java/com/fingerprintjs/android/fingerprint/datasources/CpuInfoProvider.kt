package com.fingerprintjs.android.fingerprint.datasources


import java.io.File
import java.util.Scanner
import kotlin.collections.HashMap


interface CpuInfoProvider {
    fun cpuInfo(): Map<String, String>
}

class CpuInfoProviderImpl : CpuInfoProvider {
    override fun cpuInfo(): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        try {
            val s = Scanner(File("/proc/cpuinfo"))
            while (s.hasNextLine()) {
                val cpuInfoValues = s.nextLine().split(": ")
                if (cpuInfoValues.size > 1) map[cpuInfoValues[0].trim { it <= ' ' }] =
                    cpuInfoValues[1].trim { it <= ' ' }
            }
        } catch (e: Exception) {

        }
        return map
    }
}