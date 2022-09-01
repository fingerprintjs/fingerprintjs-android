package com.fingerprintjs.android.fingerprint.signal_providers


import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo
import com.fingerprintjs.android.fingerprint.info_providers.SensorData
import com.fingerprintjs.android.fingerprint.info_providers.CpuInfo
import kotlin.reflect.KClass


// maybe get rid of this class? it's not used in the library
abstract class Signal<T>(
    // used in aev for json creation
    val name: String,
    val value: T,
) {
    // what is the purpose of such return type? Any gives us no useful information
    abstract fun toMap(): Map<String, Any>
}

//sealed class OssSignal<T> {
//    abstract fun someFunc(): String
//}
//
//sealed class FingeprintSignal {
//    class OssSignalWrapper(val ossSignal: OssSignal<*>): FingeprintSignal()
//    class OssSignalWrapper2(val ossSignal: OssSignal<*>): FingeprintSignal()
//}
//
//class OssSignalWrapper3(val ossSignal: OssSignal<*>): FingeprintSignal()
//class OssSignalWrapper4(val ossSignal: OssSignal<*>): FingeprintSignal()
//
//fun f(v: FingeprintSignal) {
//    val v = object : FingeprintSignal() {
//
//    }
//    when (v) {
//        is FingeprintSignal.OssSignalWrapper -> TODO()
//        is FingeprintSignal.OssSignalWrapper2 -> when(v.ossSignal) {
//            is SomeChild1 -> TODO()
//            is SomeChild2 -> TODO()
//        }
//        is OssSignalWrapper3 -> TODO()
//        is OssSignalWrapper4 -> TODO()
//    }
//}
//
//fun OssSignal<*>.name(): String {
//    return when(this) {
//        is SomeChild1 -> "abc"
//        is SomeChild2 -> "abc"
//    }
//}
//
//class SomeChild1 : OssSignal<String>() {
//    override fun someFunc(): String {
//        TODO("Not yet implemented")
//    }
//}
//
//class SomeChild2 : OssSignal<String>() {
//    override fun someFunc(): String {
//        TODO("Not yet implemented")
//    }
//}
//
//fun someFunc(cls: OssSignal<*>) {
//    when (cls) {
//        is SomeChild1 -> TODO()
//        is SomeChild2 -> TODO()
//    }
//}
//
//fun f(signal: IdentificationSignal<*>) {
//    when(signal) {
//
//    }
//}
//
//fun collectSignals(version: Int) {
//    val signalCreators: List<>
//}
//
//sealed class IdentificationSignal2<T> {
//    // problem: cant get these values before instance creation
//    abstract val addedInVersion: Int
//    abstract val removedInVersion: Int?
//    abstract val stabilityLevel: StabilityLevel
//    abstract val value: T
//}
//
//class SomeSignal: IdentificationSignal2<String> {
//
//}
//
//fun createSignals(
//    requestedVersion: Int,
//    requestedStabilityLevel: StabilityLevel,
//): List<IdentificationSignal2<*>> {
//    return listOfNotNull(
//        createSignalIfNeeded(
//            addedInVersion = 1,
//            removedInVersion = null,
//            stabilityLevel = StabilityLevel.STABLE,
//            requestedVersion = requestedVersion,
//            requestedStabilityLevel = requestedStabilityLevel
//        ) { _, _ -> SomeSignal() }
//    )
//}
//
//fun createSignalIfNeeded(
//    addedInVersion: Int,
//    removedInVersion: Int?,
//    stabilityLevel: StabilityLevel,
//    requestedVersion: Int,
//    requestedStabilityLevel: StabilityLevel,
//    creator: (addedInVersion: Int, removedInVersion: Int?) -> IdentificationSignal2<*>
//): IdentificationSignal2<*>? {
//    return if (requestedVersion >= addedInVersion && (removedInVersion == null || removedInVersion > requestedVersion)
//        && stabilityLevel == StabilityLevel.STABLE // check this
//    ) {
//        creator(addedInVersion, removedInVersion)
//    }
//    else {
//        null
//    }


// out T
abstract class IdentificationSignal<T>(
    // why in constructor? same signal cannot have different versions
    val addedInVersion: Int,
    val removedInVersion: Int?,
    val stabilityLevel: StabilityLevel,
    // maybe get rid of this?
    name: String,
    // maybe get rid of this?
    val displayName: String,
    value: T
    ) : Signal<T>(
    name, value
) {
    // for hashing and ui
    // we probably don't need this, but if we decide to delegate it, we would need
    // IdentificationSignal to be
    abstract override fun toString(): String

    // in aev for sending to server
    // maybe used somewhere else
    override fun toMap() = wrapSignalToMap(this)

    // why this class knows about any type it can contain?
    // why isn't it possible to override toMap() in child classes?

    // CpuInfo is unlikely to be wrapped properly
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