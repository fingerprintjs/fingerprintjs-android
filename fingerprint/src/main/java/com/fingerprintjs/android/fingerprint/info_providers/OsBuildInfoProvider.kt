package com.fingerprintjs.android.fingerprint.info_providers


import android.os.Build
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safe


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
        return safe { Build.MODEL!! }.getOrDefault("")
    }

    override fun manufacturerName(): String {
        return safe { Build.MANUFACTURER!! }.getOrDefault("")
    }

    override fun androidVersion(): String {
        return safe { Build.VERSION.RELEASE!! }.getOrDefault("")
    }

    override fun sdkVersion(): String {
        return safe { Build.VERSION.SDK_INT.toString() }.getOrDefault("")
    }

    override fun kernelVersion(): String {
        return safe { System.getProperty("os.version")!! }.getOrDefault("")
    }

    override fun fingerprint(): String {
        return safe { Build.FINGERPRINT!! }.getOrDefault("")
    }
}