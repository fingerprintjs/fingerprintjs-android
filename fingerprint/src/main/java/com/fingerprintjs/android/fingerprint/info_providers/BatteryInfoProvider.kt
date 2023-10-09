package com.fingerprintjs.android.fingerprint.info_providers


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.threading.safe.safeWithTimeout


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface BatteryInfoProvider {
    public fun batteryHealth(): String
    public fun batteryTotalCapacity(): String
}

internal class BatteryInfoProviderImpl(
        private val applicationContext: Context
) : BatteryInfoProvider {
    override fun batteryHealth(): String {
        return safeWithTimeout {
            val intent = applicationContext
                .registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!

            val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)

            if (health != -1) {
                batteryHealthStringDescription(health)
            } else {
                ""
            }
        }.getOrDefault("")
    }

    @SuppressLint("PrivateApi")
    override fun batteryTotalCapacity(): String {
        return safeWithTimeout {
            val mPowerProfile = Class.forName(POWER_PROFILE_CLASS_NAME)
                .getConstructor(Context::class.java)
                .newInstance(applicationContext)
            val batteryCapacity = Class
                .forName(POWER_PROFILE_CLASS_NAME)
                .getMethod(BATTERY_CAPACITY_METHOD_NAME)
                .invoke(mPowerProfile) as Double
            batteryCapacity.toString()
        }.getOrDefault("")
    }

    private fun batteryHealthStringDescription(batteryHealth: Int) = when(batteryHealth) {
        BatteryManager.BATTERY_HEALTH_GOOD -> "good"
        BatteryManager.BATTERY_HEALTH_OVERHEAT -> "overheat"
        BatteryManager.BATTERY_HEALTH_COLD -> "cold"
        BatteryManager.BATTERY_HEALTH_DEAD -> "dead"
        BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "over voltage"
        BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "unspecified failure"
        else -> "unknown"
    }
}

private const val POWER_PROFILE_CLASS_NAME = "com.android.internal.os.PowerProfile"
private const val BATTERY_CAPACITY_METHOD_NAME = "getBatteryCapacity"
