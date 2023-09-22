package com.fingerprintjs.android.fingerprint.info_providers


import android.app.ActivityManager
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.safe.SafeLazy
import com.fingerprintjs.android.fingerprint.tools.safe.safe


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface GpuInfoProvider {
    public fun glesVersion(): String
}

internal class GpuInfoProviderImpl(
    private val activityManager: SafeLazy<ActivityManager>,
) : GpuInfoProvider {
    override fun glesVersion(): String {
        return safe {
            activityManager.getOrThrow().deviceConfigurationInfo!!.glEsVersion!!
        }.getOrDefault("")
    }
}