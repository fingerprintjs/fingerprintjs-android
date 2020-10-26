package com.fingerprintjs.android.fingerprint.fingerprinters


import com.fingerprintjs.android.fingerprint.datasources.CpuInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.MemInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.hashers.Hasher
import java.lang.StringBuilder


class HardwareFingerprinter(
    private val cpuInfoProvider: CpuInfoProvider,
    private val memInfoProvider: MemInfoProvider,
    private val osBuildInfoProvider: OsBuildInfoProvider,
    private val hasher: Hasher,
    version: Int
) : Fingerprinter(
    version
) {
    override fun calculate(): String {
        return when (version) {
            1 -> v1()
            else -> v1()
        }
    }

    private fun v1(): String {
        val sb = StringBuilder()
        sb.append(osBuildInfoProvider.manufacturerName())
        sb.append(osBuildInfoProvider.modelName())
        sb.append(memInfoProvider.totalRAM())
        sb.append(memInfoProvider.totalInternalStorageSpace())
        cpuInfoProvider.cpuInfo().entries.forEach {
            sb.append(it.key).append(it.value)
        }

        return hasher.hash(sb.toString())
    }
}