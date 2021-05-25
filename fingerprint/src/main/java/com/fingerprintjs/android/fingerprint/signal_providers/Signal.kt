package com.fingerprintjs.android.fingerprint.signal_providers


import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo
import com.fingerprintjs.android.fingerprint.info_providers.SensorData


abstract class Signal<T>(
    val name: String,
    val value: T
) {
    abstract fun toMap(): Map<String, Any>
}


abstract class IdentificationSignal<T>(
    val addedInVersion: Int,
    val removedInVersion: Int?,
    val stabilityLevel: StabilityLevel,
    name: String,
    val displayName: String,
    value: T
    ) : Signal<T>(
    name, value
) {
    abstract override fun toString(): String

    override fun toMap() = wrapSignalToMap(this)

    private fun wrapSignalToMap(signal: Signal<*>): Map<String, Any> {
        return when (val value = signal.value ?: emptyMap<String, Any>()) {
            is String -> mapOf(
                VALUE_KEY to value
            )
            is Int -> mapOf(
                VALUE_KEY to value
            )
            is Long -> mapOf(
                VALUE_KEY to value
            )
            is Boolean -> mapOf(
                VALUE_KEY to value
            )
            is Map<*, *> -> mapOf(
                VALUE_KEY to value
            )
            is List<*> -> {
                val listValue = value.map {
                    when (it) {
                        is MediaCodecInfo -> {
                            mapOf(
                                "codecName" to it.name,
                                "codecCapabilities" to it.capabilities
                            )
                        }
                        is InputDeviceData -> {
                            mapOf("vendor" to it.vendor, "name" to it.name)
                        }
                        is SensorData -> {
                            mapOf(
                                "sensorName" to it.sensorName,
                                "vendorName" to it.vendorName
                            )
                        }
                        is CameraInfo -> {
                            mapOf(
                                "cameraName" to it.cameraName,
                                "cameraType" to it.cameraType,
                                "cameraOrientation" to it.cameraOrientation
                            )
                        }
                        is Pair<*, *> -> listOf(
                            it.first.toString(),
                            it.second.toString()
                        )
                        else -> {
                            it.toString()
                        }
                    }
                }
                mapOf(
                    VALUE_KEY to listValue
                )
            }
            else -> {
                mapOf(
                    VALUE_KEY to value.toString()
                )
            }
        }
    }
}

private const val VALUE_KEY = "v"