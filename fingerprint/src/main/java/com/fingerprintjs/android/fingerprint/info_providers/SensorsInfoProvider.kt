package com.fingerprintjs.android.fingerprint.info_providers


import android.hardware.Sensor
import android.hardware.SensorManager
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.executeSafe


public class SensorData(
    public val sensorName: String,
    public val vendorName: String
)

@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface SensorDataSource {
    public fun sensors(): List<SensorData>
}

internal class SensorDataSourceImpl(
    private val sensorManager: SensorManager
) : SensorDataSource {
    override fun sensors(): List<SensorData> {
        return executeSafe(
            {
                sensorManager.getSensorList(Sensor.TYPE_ALL).map {
                    SensorData(
                        it.name,
                        it.vendor
                    )
                }
            }, emptyList()
        )
    }
}