package com.fingerprintjs.android.fingerprint.info_providers


import android.os.Build
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.executeSafe


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface OsBuildInfoProvider {
    public fun modelName(): String
    public fun manufacturerName(): String
    public fun androidVersion(): String
    public fun sdkVersion(): String
    public fun kernelVersion(): String
    public fun fingerprint(): String
}

internal class OsBuildInfoProviderImpl :
    OsBuildInfoProvider {
    override fun modelName(): String {
        return executeSafe({ Build.MODEL }, "")
    }

    override fun manufacturerName(): String {
        return executeSafe({ Build.MANUFACTURER }, "")
    }

    override fun androidVersion(): String {
        return executeSafe({ Build.VERSION.RELEASE }, "")
    }

    override fun sdkVersion(): String {
        return executeSafe({ Build.VERSION.SDK_INT.toString() }, "")
    }

    override fun kernelVersion(): String {
        return executeSafe({ System.getProperty("os.version") ?: "" }, "")
    }

    override fun fingerprint(): String {
        return executeSafe({ Build.FINGERPRINT }, "")
    }
}