package com.fingerprintjs.android.fingerprint.datasources


import android.hardware.Sensor
import android.hardware.SensorManager


data class SensorData(
    val sensorName: String,
    val vendorName: String
)

interface SensorDataSource {
    fun sensors(): List<SensorData>
}

class SensorDataSourceImpl(
    private val sensorManager: SensorManager
) : SensorDataSource {
    override fun sensors(): List<SensorData> {
        return sensorManager.getSensorList(Sensor.TYPE_ALL).map {
            SensorData(it.name, it.vendor)
        }
    }
}