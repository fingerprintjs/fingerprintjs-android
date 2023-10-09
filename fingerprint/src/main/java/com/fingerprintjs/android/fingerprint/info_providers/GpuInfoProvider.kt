package com.fingerprintjs.android.fingerprint.info_providers


import android.app.ActivityManager
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safeWithTimeout


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface GpuInfoProvider {
    public fun glesVersion(): String
}

internal class GpuInfoProviderImpl(
    private val activityManager: ActivityManager?,
) : GpuInfoProvider {
    override fun glesVersion(): String {
        return safeWithTimeout {
            activityManager!!.deviceConfigurationInfo!!.glEsVersion!!
        }.getOrDefault("")
    }
}