package com.fingerprintjs.android.fingerprint.tools

import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.*
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

internal object FingerprintingLegacySchemeSupportExtensions {
    fun FingerprintingSignalsProvider.getHardwareSignals(
        version: Fingerprinter.Version,
        stabilityLevel: StabilityLevel,
    ): List<FingerprintingSignal<*>> {
        require(version < Fingerprinter.Version.fingerprintingFlattenedSignalsFirstVersion)
        return when (version) {
            Fingerprinter.Version.V_1 -> {
                listOf(
                    ManufacturerNameSignal.info to { manufacturerNameSignal },
                    ModelNameSignal.info to { modelNameSignal },
                    TotalRamSignal.info to { totalRamSignal },
                    TotalInternalStorageSpaceSignal.info to { totalInternalStorageSpaceSignal },
                    ProcCpuInfoSignal.info to { procCpuInfoSignal },
                    SensorsSignal.info to { sensorsSignal },
                    InputDevicesSignal.info to { inputDevicesSignal },
                )
            }
            in Fingerprinter.Version.V_2..Fingerprinter.Version.V_3 -> {
                listOf(
                    ManufacturerNameSignal.info to { manufacturerNameSignal },
                    ModelNameSignal.info to { modelNameSignal },
                    TotalRamSignal.info to { totalRamSignal },
                    TotalInternalStorageSpaceSignal.info to { totalInternalStorageSpaceSignal },
                    ProcCpuInfoSignal.info to { procCpuInfoSignal },
                    SensorsSignal.info to { sensorsSignal },
                    InputDevicesSignal.info to { inputDevicesSignal },
                    BatteryFullCapacitySignal.info to { batteryFullCapacitySignal },
                    BatteryHealthSignal.info to { batteryHealthSignal },
                    GlesVersionSignal.info to { glesVersionSignal },
                    AbiTypeSignal.info to { abiTypeSignal },
                    CoresCountSignal.info to { coresCountSignal },
                    CameraListSignal.info to { cameraListSignal },
                )
            }
            else -> {
                listOf(
                    ManufacturerNameSignal.info to { manufacturerNameSignal },
                    ModelNameSignal.info to { modelNameSignal },
                    TotalRamSignal.info to { totalRamSignal },
                    TotalInternalStorageSpaceSignal.info to { totalInternalStorageSpaceSignal },
                    ProcCpuInfoSignal.info to { procCpuInfoSignal },
                    ProcCpuInfoV2Signal.info to { procCpuInfoV2Signal },
                    SensorsSignal.info to { sensorsSignal },
                    InputDevicesSignal.info to { inputDevicesSignal },
                    InputDevicesV2Signal.info to { inputDevicesV2Signal },
                    BatteryHealthSignal.info to { batteryHealthSignal },
                    BatteryFullCapacitySignal.info to { batteryFullCapacitySignal },
                    CameraListSignal.info to { cameraListSignal },
                    GlesVersionSignal.info to { glesVersionSignal },
                    AbiTypeSignal.info to { abiTypeSignal },
                    CoresCountSignal.info to { coresCountSignal },
                )
            }
        }.mapNotNull {
            SignalsUtils.createSignalIfNeeded(
                requiredVersion = version,
                requiredStabilityLevel = stabilityLevel,
                signalFingerprintingInfo = it.first,
                signalFactory = it.second,
            )
        }
    }

    fun FingerprintingSignalsProvider.getOsBuildSignals(
        version: Fingerprinter.Version,
        stabilityLevel: StabilityLevel,
    ): List<FingerprintingSignal<*>> {
        require(version < Fingerprinter.Version.fingerprintingFlattenedSignalsFirstVersion)
        return when (version) {
            Fingerprinter.Version.V_1 -> {
                listOf(
                    FingerprintSignal.info to { fingerprintSignal },
                )
            }
            else -> {
                listOf(
                    AndroidVersionSignal.info to { androidVersionSignal },
                    SdkVersionSignal.info to { sdkVersionSignal },
                    KernelVersionSignal.info to { kernelVersionSignal },
                    EncryptionStatusSignal.info to { encryptionStatusSignal },
                    SecurityProvidersSignal.info to { securityProvidersSignal },
                    CodecListSignal.info to { codecListSignal },
                )
            }
        }.mapNotNull {
            SignalsUtils.createSignalIfNeeded(
                requiredVersion = version,
                requiredStabilityLevel = stabilityLevel,
                signalFingerprintingInfo = it.first,
                signalFactory = it.second,
            )
        }
    }

    fun FingerprintingSignalsProvider.getDeviceStateSignals(
        version: Fingerprinter.Version,
        stabilityLevel: StabilityLevel,
    ): List<FingerprintingSignal<*>> {
        require(version < Fingerprinter.Version.fingerprintingFlattenedSignalsFirstVersion)
        val overriddenStabilityLevel = when(version) {
            Fingerprinter.Version.V_1 -> StabilityLevel.UNIQUE
            else ->stabilityLevel
        }
        return when (version) {
            Fingerprinter.Version.V_1 -> {
                listOf(
                    AdbEnabledSignal.info to { adbEnabledSignal },
                    DevelopmentSettingsEnabledSignal.info to { developmentSettingsEnabledSignal },
                    HttpProxySignal.info to { httpProxySignal },
                    TransitionAnimationScaleSignal.info to { transitionAnimationScaleSignal },
                    WindowAnimationScaleSignal.info to { windowAnimationScaleSignal },
                    DataRoamingEnabledSignal.info to { dataRoamingEnabledSignal },
                    AccessibilityEnabledSignal.info to { accessibilityEnabledSignal },
                    DefaultInputMethodSignal.info to { defaultInputMethodSignal },
                    RttCallingModeSignal.info to { rttCallingModeSignal },
                    TouchExplorationEnabledSignal.info to { touchExplorationEnabledSignal },
                    AlarmAlertPathSignal.info to { alarmAlertPathSignal },
                    DateFormatSignal.info to { dateFormatSignal },
                    EndButtonBehaviourSignal.info to { endButtonBehaviourSignal },
                    FontScaleSignal.info to { fontScaleSignal },
                    ScreenOffTimeoutSignal.info to { screenOffTimeoutSignal },
                    TextAutoReplaceEnabledSignal.info to { textAutoReplaceEnabledSignal },
                    TextAutoPunctuateSignal.info to { textAutoPunctuateSignal },
                    Time12Or24Signal.info to { time12Or24Signal },
                    IsPinSecurityEnabledSignal.info to { isPinSecurityEnabledSignal },
                    FingerprintSensorStatusSignal.info to { fingerprintSensorStatusSignal },
                    RingtoneSourceSignal.info to { ringtoneSourceSignal },
                    AvailableLocalesSignal.info to { availableLocalesSignal },
                )
            }
            else -> {
                listOf(
                    AdbEnabledSignal.info to { adbEnabledSignal },
                    DevelopmentSettingsEnabledSignal.info to { developmentSettingsEnabledSignal },
                    HttpProxySignal.info to { httpProxySignal },
                    TransitionAnimationScaleSignal.info to { transitionAnimationScaleSignal },
                    WindowAnimationScaleSignal.info to { windowAnimationScaleSignal },
                    DataRoamingEnabledSignal.info to { dataRoamingEnabledSignal },
                    AccessibilityEnabledSignal.info to { accessibilityEnabledSignal },
                    DefaultInputMethodSignal.info to { defaultInputMethodSignal },
                    TouchExplorationEnabledSignal.info to { touchExplorationEnabledSignal },
                    AlarmAlertPathSignal.info to { alarmAlertPathSignal },
                    DateFormatSignal.info to { dateFormatSignal },
                    EndButtonBehaviourSignal.info to { endButtonBehaviourSignal },
                    FontScaleSignal.info to { fontScaleSignal },
                    ScreenOffTimeoutSignal.info to { screenOffTimeoutSignal },
                    Time12Or24Signal.info to { time12Or24Signal },
                    IsPinSecurityEnabledSignal.info to { isPinSecurityEnabledSignal },
                    FingerprintSensorStatusSignal.info to { fingerprintSensorStatusSignal },
                    RingtoneSourceSignal.info to { ringtoneSourceSignal },
                    AvailableLocalesSignal.info to { availableLocalesSignal },
                    RegionCountrySignal.info to { regionCountrySignal },
                    TimezoneSignal.info to { timezoneSignal },
                    DefaultLanguageSignal.info to { defaultLanguageSignal },
                )
            }
        }.mapNotNull {
            SignalsUtils.createSignalIfNeeded(
                requiredVersion = version,
                requiredStabilityLevel = overriddenStabilityLevel,
                signalFingerprintingInfo = it.first,
                signalFactory = it.second,
            )
        }
    }

    fun FingerprintingSignalsProvider.getInstalledAppsSignals(
        version: Fingerprinter.Version,
        stabilityLevel: StabilityLevel,
    ): List<FingerprintingSignal<*>> {
        require(version < Fingerprinter.Version.fingerprintingFlattenedSignalsFirstVersion)
        val overriddenStabilityLevel = when(version) {
            Fingerprinter.Version.V_1 -> StabilityLevel.UNIQUE
            else ->stabilityLevel
        }
        return when (version) {
            Fingerprinter.Version.V_1 -> {
                listOf(
                    ApplicationsListSignal.info to { applicationsListSignal },
                )
            }
            else -> {
                listOf(
                    ApplicationsListSignal.info to { applicationsListSignal },
                    SystemApplicationsListSignal.info to { systemApplicationsListSignal },
                )
            }
        }.mapNotNull {
            SignalsUtils.createSignalIfNeeded(
                requiredVersion = version,
                requiredStabilityLevel = overriddenStabilityLevel,
                signalFingerprintingInfo = it.first,
                signalFactory = it.second,
            )
        }
    }
}
