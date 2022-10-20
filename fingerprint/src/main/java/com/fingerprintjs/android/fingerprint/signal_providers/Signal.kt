package com.fingerprintjs.android.fingerprint.signal_providers


import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo
import com.fingerprintjs.android.fingerprint.info_providers.SensorData
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfo
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public abstract class Signal<T>(
    public val name: String,
    public val value: T
) {
    public abstract fun toMap(): Map<String, Any>
}


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public abstract class IdentificationSignal<T>(
    public val addedInVersion: Int,
    public val removedInVersion: Int?,
    public val stabilityLevel: StabilityLevel,
    name: String,
    public val displayName: String,
    value: T
    ) : Signal<T>(
    name, value
) {
    abstract override fun toString(): String

    override fun toMap(): Map<String, Any> = wrapSignalToMap(this)

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
            is CpuInfo -> {
                val commonProps = value.commonInfo
                val procCount = value.perProcessorInfo.size
                val perProcRepeatedProps = value.perProcessorInfo
                    .flatten()
                    .groupBy { it }
                    .filter { it.value.size == procCount }
                    .map { it.key }
                val perProcRepeatedPropsKeys = perProcRepeatedProps.map { it.first }.toSet()
                val perProcUniqueProps = value.perProcessorInfo
                    .map { procInfo -> procInfo.filter { it.first !in perProcRepeatedPropsKeys } }
                mapOf(
                    VALUE_KEY to mapOf(
                        "commonProps" to commonProps,
                        "repeatedProps" to perProcRepeatedProps,
                        "uniquePerCpuProps" to perProcUniqueProps,
                    )
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