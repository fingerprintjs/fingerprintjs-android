package com.fingerprintjs.android.fingerprint.info_providers


import android.app.ActivityManager
import android.os.StatFs
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safeWithTimeout


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface MemInfoProvider {
    public fun totalRAM(): Long
    public fun totalInternalStorageSpace(): Long
    public fun totalExternalStorageSpace(): Long
}

internal class MemInfoProviderImpl(
    private val activityManager: ActivityManager?,
    private val internalStorageStats: StatFs?,
    private val externalStorageStats: StatFs?,
) : MemInfoProvider {
    override fun totalRAM(): Long {
        return safeWithTimeout {
                val memoryInfo = ActivityManager.MemoryInfo()
                activityManager!!.getMemoryInfo(memoryInfo)
                memoryInfo.totalMem
            }.getOrDefault(0)
    }

    override fun totalInternalStorageSpace(): Long {
        return safeWithTimeout { internalStorageStats!!.totalBytes }.getOrDefault(0)
    }

    override fun totalExternalStorageSpace(): Long {
        return safeWithTimeout { externalStorageStats!!.totalBytes }.getOrDefault(0)
    }
}