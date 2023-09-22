package com.fingerprintjs.android.fingerprint.info_providers


import android.app.ActivityManager
import android.os.StatFs
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.safe.SafeLazy
import com.fingerprintjs.android.fingerprint.tools.safe.safe


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface MemInfoProvider {
    public fun totalRAM(): Long
    public fun totalInternalStorageSpace(): Long
    public fun totalExternalStorageSpace(): Long
}

internal class MemInfoProviderImpl(
    private val activityManager: SafeLazy<ActivityManager>,
    private val internalStorageStats: SafeLazy<StatFs>,
    private val externalStorageStats: SafeLazy<StatFs>,
) : MemInfoProvider {
    override fun totalRAM(): Long {
        return safe {
                val memoryInfo = ActivityManager.MemoryInfo()
                activityManager.getOrThrow().getMemoryInfo(memoryInfo)
                memoryInfo.totalMem
            }.getOrDefault(0)
    }

    override fun totalInternalStorageSpace(): Long {
        return safe { internalStorageStats.getOrThrow().totalBytes }.getOrDefault(0)
    }

    override fun totalExternalStorageSpace(): Long {
        return safe { externalStorageStats.getOrThrow().totalBytes }.getOrDefault(0)
    }
}