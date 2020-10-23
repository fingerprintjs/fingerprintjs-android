package com.fingerprintjs.android.fingerprint.datasources


import android.app.ActivityManager
import android.os.StatFs


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
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem
    }

    override fun totalInternalStorageSpace(): Long {
        return internalStorageStats.totalBytes
    }

    override fun totalExternalStorageSpace(): Long {
        return externalStorageStats.totalBytes
    }
}