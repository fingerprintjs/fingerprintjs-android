package com.fingerprintjs.android.fingerprint.datasources


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface BatteryInfoDataSource {
    fun batteryHealth(): String
    fun batteryTotalCapacity(): String
}

class BatteryInfoDataSourceImpl(
        private val applicationContext: Context
) : BatteryInfoDataSource {
    override fun batteryHealth(): String {
        val intent = applicationContext
                .registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED)) ?: return ""

        val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)

        return if (health != -1) {
            health.toString()
        } else {
            ""
        }
    }

    @SuppressLint("PrivateApi")
    override fun batteryTotalCapacity(): String {

        return executeSafe({
            val mPowerProfile = Class.forName(POWER_PROFILE_CLASS_NAME)
                    .getConstructor(Context::class.java)
                    .newInstance(applicationContext)
            val batteryCapacity = Class
                    .forName(POWER_PROFILE_CLASS_NAME)
                    .getMethod(BATTERY_CAPACITY_METHOD_NAME)
                    .invoke(mPowerProfile) as Double

            batteryCapacity.toString()
        }, "")
    }
}

private const val POWER_PROFILE_CLASS_NAME = "com.android.internal.os.PowerProfile"
private const val BATTERY_CAPACITY_METHOD_NAME = "getBatteryCapacity"
