package com.fingerprintjs.android.fingerprint.datasources


import android.app.ActivityManager
import android.os.Build
import android.os.StatFs
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface MemInfoProvider {
    fun totalRAM(): Long
    fun totalInternalStorageSpace(): Long
    fun totalExternalStorageSpace(): Long
}

class MemInfoProviderImpl(
    private val activityManager: ActivityManager,
    private val internalStorageStats: StatFs,
    private val externalStorageStats: StatFs
) : MemInfoProvider {
    override fun totalRAM(): Long {
        return executeSafe(
            {
                val memoryInfo = ActivityManager.MemoryInfo()
                activityManager.getMemoryInfo(memoryInfo)
                memoryInfo.totalMem
            }, 0
        )
    }

    override fun totalInternalStorageSpace(): Long {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return (internalStorageStats.blockSize * internalStorageStats.blockCount).toLong()
        }
        return executeSafe(
            { internalStorageStats.totalBytes },
            0
        )
    }

    override fun totalExternalStorageSpace(): Long {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return (externalStorageStats.blockSize * externalStorageStats.blockCount).toLong()
        }
        return executeSafe(
            { externalStorageStats.totalBytes },
            0
        )
    }
}