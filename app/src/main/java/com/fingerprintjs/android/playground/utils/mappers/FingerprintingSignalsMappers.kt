package com.fingerprintjs.android.playground.utils.mappers

import com.fingerprintjs.android.fingerprint.fingerprinting_signals.*
import com.fingerprintjs.android.playground.constants.Constants
import com.google.gson.annotations.SerializedName

val FingerprintingSignal<*>.humanName: String
    get() = when (this) {
        is AbiTypeSignal -> "Abi Type"
        is AccessibilityEnabledSignal -> "Accessibility Enabled"
        is AdbEnabledSignal -> "Adb Enabled"
        is AlarmAlertPathSignal -> "Alarm Alert Path"
        is AndroidVersionSignal -> "Android Version"
        is ApplicationsListSignal -> "Applications List"
        is AvailableLocalesSignal -> "Available Locales"
        is BatteryFullCapacitySignal -> "Battery Full Capacity"
        is BatteryHealthSignal -> "Battery Health"
        is CameraListSignal -> "Camera List"
        is CodecListSignal -> "Codec List"
        is CoresCountSignal -> "Cores Count"
        is DataRoamingEnabledSignal -> "Data Roaming Enabled"
        is DateFormatSignal -> "Date Format"
        is DefaultInputMethodSignal -> "Default Input Method"
        is DefaultLanguageSignal -> "Default Language"
        is DevelopmentSettingsEnabledSignal -> "Development Settings Enabled"
        is EncryptionStatusSignal -> "Encryption Status"
        is EndButtonBehaviourSignal -> "End Button Behaviour"
        is FingerprintSensorStatusSignal -> "Fingerprint Sensor Status"
        is FingerprintSignal -> "Fingerprint"
        is FontScaleSignal -> "Font Scale"
        is GlesVersionSignal -> "Gles Version"
        is HttpProxySignal -> "Http Proxy"
        is InputDevicesSignal -> "Input Devices"
        is InputDevicesV2Signal -> "Input Devices V2"
        is IsPinSecurityEnabledSignal -> "Is Pin Security Enabled"
        is KernelVersionSignal -> "Kernel Version"
        is ManufacturerNameSignal -> "Manufacturer Name"
        is ModelNameSignal -> "Model Name"
        is ProcCpuInfoSignal -> "ProcCpuInfo"
        is ProcCpuInfoV2Signal -> "ProcCpuInfo V2"
        is RegionCountrySignal -> "Region Country"
        is RingtoneSourceSignal -> "Ringtone Source"
        is RttCallingModeSignal -> "Rtt Calling Mode"
        is ScreenOffTimeoutSignal -> "Screen Off Timeout"
        is SdkVersionSignal -> "Sdk Version"
        is SecurityProvidersSignal -> "Security Providers"
        is SensorsSignal -> "Sensors"
        is SystemApplicationsListSignal -> "System Applications List"
        is TextAutoPunctuateSignal -> "Text Auto Punctuate"
        is TextAutoReplaceEnabledSignal -> "Text Auto Replace Enabled"
        is Time12Or24Signal -> "Time 12 Or 24"
        is TimezoneSignal -> "Timezone"
        is TotalInternalStorageSpaceSignal -> "Total Internal Storage Space"
        is TotalRamSignal -> "Total Ram"
        is TouchExplorationEnabledSignal -> "Touch Exploration Enabled"
        is TransitionAnimationScaleSignal -> "Transition Animation Scale"
        is WindowAnimationScaleSignal -> "Window Animation Scale"
    }

/**
 * Anything convertible to json via Gson
 */
val FingerprintingSignal<*>.jsonifiableValue: Any
    get() = when (this) {
        is AbiTypeSignal -> this.value
        is AccessibilityEnabledSignal -> this.value
        is AdbEnabledSignal -> this.value
        is AlarmAlertPathSignal -> this.value
        is AndroidVersionSignal -> this.value
        is ApplicationsListSignal -> this.value.map { it.packageName }
        is AvailableLocalesSignal -> this.value
        is BatteryFullCapacitySignal -> this.value
        is BatteryHealthSignal -> this.value
        is CameraListSignal -> this.value.map {
            CameraListSignalItemVo(
                cameraName = it.cameraName,
                cameraType = it.cameraType,
                cameraOrientation = it.cameraOrientation,
            )
        }
        is CodecListSignal -> this.value.map {
            CodecListSignalItemVo(
                name = it.name,
                capabilities = it.capabilities
            )
        }
        is CoresCountSignal -> this.value.toString()
        is DataRoamingEnabledSignal -> this.value
        is DateFormatSignal -> this.value
        is DefaultInputMethodSignal -> this.value
        is DefaultLanguageSignal -> this.value
        is DevelopmentSettingsEnabledSignal -> this.value
        is EncryptionStatusSignal -> this.value
        is EndButtonBehaviourSignal -> this.value
        is FingerprintSensorStatusSignal -> this.value
        is FingerprintSignal -> this.value
        is FontScaleSignal -> this.value
        is GlesVersionSignal -> this.value
        is HttpProxySignal -> this.value
        is InputDevicesSignal -> this.value.map {
            InputDevicesSignalItemVo(
                name = it.name,
                vendor = it.vendor
            )
        }
        is InputDevicesV2Signal -> this.value.map {
            InputDevicesSignalItemVo(
                name = it.name,
                vendor = it.vendor
            )
        }
        is IsPinSecurityEnabledSignal -> this.value.toString()
        is KernelVersionSignal -> this.value
        is ManufacturerNameSignal -> this.value
        is ModelNameSignal -> this.value
        is ProcCpuInfoSignal -> this.value
        is ProcCpuInfoV2Signal -> {
            val perProcessorInfoList = this.value.perProcessorInfo.mapIndexed { index, pairs ->
                listOf("Processor" to index.toString()) + pairs
            }.flatten()
            this.value.commonInfo + perProcessorInfoList
        }
        is RegionCountrySignal -> this.value
        is RingtoneSourceSignal -> this.value
        is RttCallingModeSignal -> this.value
        is ScreenOffTimeoutSignal -> this.value
        is SdkVersionSignal -> this.value
        is SecurityProvidersSignal -> this.value
        is SensorsSignal -> this.value.map {
            SensorsSignalItemVo(
                sensorName = it.sensorName,
                vendorName = it.vendorName
            )
        }
        is SystemApplicationsListSignal -> this.value.map { it.packageName }
        is TextAutoPunctuateSignal -> this.value
        is TextAutoReplaceEnabledSignal -> this.value
        is Time12Or24Signal -> this.value
        is TimezoneSignal -> this.value
        is TotalInternalStorageSpaceSignal -> this.value.toString()
        is TotalRamSignal -> this.value.toString()
        is TouchExplorationEnabledSignal -> this.value
        is TransitionAnimationScaleSignal -> this.value
        is WindowAnimationScaleSignal -> this.value
    }

val FingerprintingSignal<*>.humanValue: String
    get() = when (this) {
        is AbiTypeSignal -> valueAsSimpleString()
        is AccessibilityEnabledSignal -> valueAsSimpleString()
        is AdbEnabledSignal -> valueAsSimpleString()
        is AlarmAlertPathSignal -> valueAsSimpleString()
        is AndroidVersionSignal -> valueAsSimpleString()
        is ApplicationsListSignal -> valueAsSimpleListString { appendLine(it) }
        is AvailableLocalesSignal -> valueAsSimpleListString { appendLine(it) }
        is BatteryFullCapacitySignal -> valueAsSimpleString()
        is BatteryHealthSignal -> valueAsSimpleString()
        is CameraListSignal -> valueAsSeparatedListString {
            appendLine(it.cameraName)
            appendLine(it.cameraType)
            appendLine(it.cameraOrientation)
        }
        is CodecListSignal -> valueAsSeparatedListString {
            appendLine(it.name)
            appendLine(it.capabilities)
        }
        is CoresCountSignal -> valueAsSimpleString()
        is DataRoamingEnabledSignal -> valueAsSimpleString()
        is DateFormatSignal -> valueAsSimpleString()
        is DefaultInputMethodSignal -> valueAsSimpleString()
        is DefaultLanguageSignal -> valueAsSimpleString()
        is DevelopmentSettingsEnabledSignal -> valueAsSimpleString()
        is EncryptionStatusSignal -> valueAsSimpleString()
        is EndButtonBehaviourSignal -> valueAsSimpleString()
        is FingerprintSensorStatusSignal -> valueAsSimpleString()
        is FingerprintSignal -> valueAsSimpleString()
        is FontScaleSignal -> valueAsSimpleString()
        is GlesVersionSignal -> valueAsSimpleString()
        is HttpProxySignal -> valueAsSimpleString()
        is InputDevicesSignal -> valueAsSeparatedListString {
            appendLine(it.name)
            appendLine(it.vendor)
        }
        is InputDevicesV2Signal -> valueAsSeparatedListString {
            appendLine(it.name)
            appendLine(it.vendor)
        }
        is IsPinSecurityEnabledSignal -> valueAsSimpleString()
        is KernelVersionSignal -> valueAsSimpleString()
        is ManufacturerNameSignal -> valueAsSimpleString()
        is ModelNameSignal -> valueAsSimpleString()
        is ProcCpuInfoSignal -> buildString {
            this@humanValue.value.entries.forEach {
                appendLine(it.toHumanString())
            }
        }
        is ProcCpuInfoV2Signal -> buildString {
            this@humanValue.value.commonInfo.forEach {
                appendLine("${it.first} : ${it.second}")
            }
            appendLine()
            this@humanValue.value.perProcessorInfo.forEachIndexed { index, pairs ->
                appendLine("Processor : $index")
                pairs.forEach {
                    appendLine("${it.first} : ${it.second}")
                }
                if (index != this@humanValue.value.perProcessorInfo.lastIndex)
                    appendLine()
            }
        }
        is RegionCountrySignal -> valueAsSimpleString()
        is RingtoneSourceSignal -> valueAsSimpleString()
        is RttCallingModeSignal -> valueAsSimpleString()
        is ScreenOffTimeoutSignal -> valueAsSimpleString()
        is SdkVersionSignal -> valueAsSimpleString()
        is SecurityProvidersSignal -> valueAsSimpleListString { appendLine(it.toHumanString()) }
        is SensorsSignal -> valueAsSeparatedListString {
            appendLine(it.sensorName)
            appendLine(it.vendorName)
        }
        is SystemApplicationsListSignal -> valueAsSimpleListString { appendLine(it.packageName) }
        is TextAutoPunctuateSignal -> valueAsSimpleString()
        is TextAutoReplaceEnabledSignal -> valueAsSimpleString()
        is Time12Or24Signal -> valueAsSimpleString()
        is TimezoneSignal -> valueAsSimpleString()
        is TotalInternalStorageSpaceSignal -> valueAsSimpleString()
        is TotalRamSignal -> valueAsSimpleString()
        is TouchExplorationEnabledSignal -> valueAsSimpleString()
        is TransitionAnimationScaleSignal -> valueAsSimpleString()
        is WindowAnimationScaleSignal -> valueAsSimpleString()
    }

@JvmName("valueAsSimpleStringString")
private fun FingerprintingSignal<String>.valueAsSimpleString(): String {
    return this.value.takeIf { it.isNotEmpty() } ?: Constants.SIGNAL_UNKNOWN_VALUE
}
@JvmName("valueAsSimpleStringBoolean")
private fun FingerprintingSignal<Boolean>.valueAsSimpleString(): String {
    return this.value.toString()
}
@JvmName("valueAsSimpleStringInt")
private fun FingerprintingSignal<Int>.valueAsSimpleString(): String {
    return this.value.toString()
}
@JvmName("valueAsSimpleStringLong")
private fun FingerprintingSignal<Long>.valueAsSimpleString(): String {
    return this.value.toString()
}

private fun <T> FingerprintingSignal<List<T>>.valueAsSimpleListString(entryStringBuilder: StringBuilder.(T) -> Unit): String {
    return buildString { this@valueAsSimpleListString.value.forEach { entryStringBuilder(it) } }
}

private fun <T> FingerprintingSignal<List<T>>.valueAsSeparatedListString(entryStringBuilder: StringBuilder.(T) -> Unit): String {
    return buildString {
        this@valueAsSeparatedListString.value.forEachIndexed { index, t ->
            entryStringBuilder(t)
            if (index != this@valueAsSeparatedListString.value.lastIndex)
                appendLine()
        }
    }
}

private fun Pair<String, String>.toHumanString() = "$first : $second"
private fun Map.Entry<String, String>.toHumanString() = (this.key to this.value).toHumanString()

data class CameraListSignalItemVo(
    @SerializedName("cameraName") val cameraName: String,
    @SerializedName("cameraType") val cameraType: String,
    @SerializedName("cameraOrientation") val cameraOrientation: String
)

data class CodecListSignalItemVo(
    @SerializedName("name") val name: String,
    @SerializedName("capabilities") val capabilities: List<String>
)

data class InputDevicesSignalItemVo(
    @SerializedName("name") val name: String,
    @SerializedName("vendor") val vendor: String
)

data class SensorsSignalItemVo(
    @SerializedName("sensorName") val sensorName: String,
    @SerializedName("vendorName") val vendorName: String
)
