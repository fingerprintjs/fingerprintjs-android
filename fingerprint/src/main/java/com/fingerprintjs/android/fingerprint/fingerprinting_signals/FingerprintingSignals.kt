package com.fingerprintjs.android.fingerprint.fingerprinting_signals

import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.info_providers.*
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel


// region: Hardware signals

public class ManufacturerNameSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class ModelNameSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class TotalRamSignal(
    override val value: Long,
) : FingerprintingSignal<Long>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class TotalInternalStorageSpaceSignal(
    override val value: Long,
) : FingerprintingSignal<Long>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class ProcCpuInfoSignal(
    override val value: Map<String, String>,
) : FingerprintingSignal<Map<String, String>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()
        value.entries.forEach {
            sb.append(it.key).append(it.value)
        }
        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = Fingerprinter.Version.V_4,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class ProcCpuInfoV2Signal(
    value: CpuInfo,
) : FingerprintingSignal<CpuInfo>() {

    override val value: CpuInfo = value.copy(
        commonInfo = value.commonInfo
            .filter { it.first.lowercase() !in CPUINFO_IGNORED_COMMON_PROPS },
        perProcessorInfo = value.perProcessorInfo
            .map { procInfo -> procInfo.filter { it.first.lowercase() !in CPUINFO_IGNORED_PER_PROC_PROPS } },
    )

    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value.commonInfo.toString() + value.perProcessorInfo.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_4,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )

        private val CPUINFO_IGNORED_COMMON_PROPS = setOf(
            "processor",
        )

        private val CPUINFO_IGNORED_PER_PROC_PROPS = setOf(
            "bogomips",
            "cpu mhz",
        )
    }
}

public class SensorsSignal(
    override val value: List<SensorData>,
) : FingerprintingSignal<List<SensorData>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()
        value.forEach {
            sb.append(it.sensorName).append(it.vendorName)
        }
        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class InputDevicesSignal(
    override val value: List<InputDeviceData>,
) : FingerprintingSignal<List<InputDeviceData>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()
        value.forEach {
            sb.append(it.name).append(it.vendor)
        }
        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = Fingerprinter.Version.V_4,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class InputDevicesV2Signal(
    override val value: List<InputDeviceData>,
) : FingerprintingSignal<List<InputDeviceData>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()
        value
            .map { "${it.name}${it.vendor}" }
            .sortedBy { it }
            .forEach { sb.append(it) }
        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_4,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class BatteryHealthSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class BatteryFullCapacitySignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class CameraListSignal(
    override val value: List<CameraInfo>,
) : FingerprintingSignal<List<CameraInfo>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()
        value.forEach {
            sb
                .append(it.cameraName)
                .append(it.cameraType)
                .append(it.cameraOrientation)
        }
        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class GlesVersionSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class AbiTypeSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

public class CoresCountSignal(
    override val value: Int,
) : FingerprintingSignal<Int>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.STABLE,
        )
    }
}

// endregion

// region: Os Build signals

public class FingerprintSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = Fingerprinter.Version.V_2,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class AndroidVersionSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class SdkVersionSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class KernelVersionSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class EncryptionStatusSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class CodecListSignal(
    override val value: List<MediaCodecInfo>,
) : FingerprintingSignal<List<MediaCodecInfo>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()

        value.forEach {
            sb.append(it.name)
            it.capabilities.forEach { capability ->
                sb.append(capability)
            }
        }

        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class SecurityProvidersSignal(
    override val value: List<Pair<String, String>>,
) : FingerprintingSignal<List<Pair<String, String>>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()

        value.forEach {
            sb
                .append(it.first)
                .append(it.second)
        }

        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

// endregion

// region: Installed apps signals

public class ApplicationsListSignal(
    override val value: List<PackageInfo>,
) : FingerprintingSignal<List<PackageInfo>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()

        value
            .sortedBy { it.packageName }
            .forEach { sb.append(it.packageName) }

        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.UNIQUE,
        )
    }
}

public class SystemApplicationsListSignal(
    override val value: List<PackageInfo>,
) : FingerprintingSignal<List<PackageInfo>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()

        value
            .sortedBy { it.packageName }
            .forEach { sb.append(it.packageName) }

        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}
// endregion

// region: Device state signals

public class AdbEnabledSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class DevelopmentSettingsEnabledSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class HttpProxySignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.UNIQUE,
        )
    }
}

public class TransitionAnimationScaleSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class WindowAnimationScaleSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class DataRoamingEnabledSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.UNIQUE,
        )
    }
}

public class AccessibilityEnabledSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class DefaultInputMethodSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class RttCallingModeSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = Fingerprinter.Version.V_2,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class TouchExplorationEnabledSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class AlarmAlertPathSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class DateFormatSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class EndButtonBehaviourSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class FontScaleSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class ScreenOffTimeoutSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class TextAutoReplaceEnabledSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = Fingerprinter.Version.V_2,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class TextAutoPunctuateSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = Fingerprinter.Version.V_2,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class Time12Or24Signal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class IsPinSecurityEnabledSignal(
    override val value: Boolean,
) : FingerprintingSignal<Boolean>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class FingerprintSensorStatusSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class RingtoneSourceSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class AvailableLocalesSignal(
    override val value: List<String>,
) : FingerprintingSignal<List<String>>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        val sb = StringBuilder()
        value.forEach {
            sb.append(it)
        }
        return sb.toString()
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_1,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class RegionCountrySignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class DefaultLanguageSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}

public class TimezoneSignal(
    override val value: String,
) : FingerprintingSignal<String>() {
    override val info: Info
        get() = Companion.info

    override fun getHashableString(): String {
        return value
    }

    public companion object {
        public val info: Info = Info(
            addedInVersion = Fingerprinter.Version.V_2,
            removedInVersion = null,
            stabilityLevel = StabilityLevel.OPTIMAL,
        )
    }
}
// endregion
