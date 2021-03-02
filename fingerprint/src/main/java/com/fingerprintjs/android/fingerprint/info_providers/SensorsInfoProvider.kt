package com.fingerprintjs.android.fingerprint.info_providers


import android.hardware.Sensor
import android.hardware.SensorManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe


data class SensorData(
    val sensorName: String,
    val vendorName: String
)

interface SensorDataSource {
    fun sensors(): List<SensorData>
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