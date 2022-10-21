package com.fingerprintjs.android.fingerprint.fingerprinting_signals

import androidx.annotation.WorkerThread
import com.fingerprintjs.android.fingerprint.IdentificationVersion
import com.fingerprintjs.android.fingerprint.info_providers.*
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.inRange

/**
 * A class that provides signals used in [Fingerprinter's][com.fingerprintjs.android.fingerprint.Fingerprinter]
 * getFingerprint(fingerprintingSignals, hashed) method.
 * You can get an instance of this class using
 * [getFingerprintingSignalsProvider][com.fingerprintjs.android.fingerprint.Fingerprinter.getFingerprintingSignalsProvider]
 * method.
 *
 * All signals in this class are cached.
 */
public class FingerprintingSignalsProvider internal constructor(
    private val cpuInfoProvider: CpuInfoProvider,
    private val memInfoProvider: MemInfoProvider,
    private val sensorsDataSource: SensorDataSource,
    private val inputDeviceDataSource: InputDeviceDataSource,
    private val batteryInfoProvider: BatteryInfoProvider,
    private val cameraInfoProvider: CameraInfoProvider,
    private val gpuInfoProvider: GpuInfoProvider,
    private val osBuildInfoProvider: OsBuildInfoProvider,
    private val codecInfoProvider: CodecInfoProvider?,
    private val deviceSecurityInfoProvider: DeviceSecurityInfoProvider,
    private val packageManagerDataSource: PackageManagerDataSource,
    private val settingsDataSource: SettingsDataSource,
    private val devicePersonalizationInfoProvider: DevicePersonalizationInfoProvider,
    private val fingerprintSensorInfoProvider: FingerprintSensorInfoProvider,
) {

    private fun <T : FingerprintingSignal<*>> createSignalIfNeeded(
        requiredVersion: IdentificationVersion,
        requiredStabilityLevel: StabilityLevel,
        signalFingerprintingInfo: FingerprintingSignal.Info,
        signalFactory: () -> T,
    ): T? {
        return if (
            signalFingerprintingInfo.stabilityLevel.atLeastAsStableAs(requiredStabilityLevel)
            && requiredVersion.inRange(signalFingerprintingInfo.addedInVersion, signalFingerprintingInfo.removedInVersion)
        )
            signalFactory.invoke()
        else
            null
    }

    /**
     * Shorthand method returning the subset of signals listed below in this class
     * with respect to provided parameters.
     *
     * Starting from [IdentificationVersion.fingerprintingFlattenedSignalsFirstVersion], calculating
     * a device fingerprint via [Fingerprinter's][com.fingerprintjs.android.fingerprint.Fingerprinter]
     * getFingerprint(fingerprintingSignals, hasher)  method using signals returned from this method will result
     * in exactly the same fingerprint as when using [Fingerprinter's][com.fingerprintjs.android.fingerprint.Fingerprinter]
     * getFingerprint(version, stabilityLevel, hasher, listener) method with the same parameters.
     *
     * @param version identification version. Check out [IdentificationVersion] for details.
     * @param stabilityLevel stability level. Check out [StabilityLevel] for details.
     */
    @WorkerThread
    public fun getSignalsMatching(
        version: IdentificationVersion,
        stabilityLevel: StabilityLevel
    ): List<FingerprintingSignal<*>> {
        return listOf(
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
            FingerprintSignal.info to { fingerprintSignal },
            AndroidVersionSignal.info to { androidVersionSignal },
            SdkVersionSignal.info to { sdkVersionSignal },
            KernelVersionSignal.info to { kernelVersionSignal },
            EncryptionStatusSignal.info to { encryptionStatusSignal },
            CodecListSignal.info to { codecListSignal },
            SecurityProvidersSignal.info to { securityProvidersSignal },
            ApplicationsListSignal.info to { applicationsListSignal },
            SystemApplicationsListSignal.info to { systemApplicationsListSignal },
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
            RegionCountrySignal.info to { regionCountrySignal },
            DefaultLanguageSignal.info to { defaultLanguageSignal },
            TimezoneSignal.info to { timezoneSignal },
        ).mapNotNull {
            createSignalIfNeeded(
                requiredVersion = version,
                requiredStabilityLevel = stabilityLevel,
                signalFingerprintingInfo = it.first,
                signalFactory = it.second
            )
        }
    }

    @get:WorkerThread
    public val manufacturerNameSignal: ManufacturerNameSignal by lazy {
        ManufacturerNameSignal(osBuildInfoProvider.manufacturerName())
    }
    @get:WorkerThread
    public val modelNameSignal: ModelNameSignal by lazy {
        ModelNameSignal(osBuildInfoProvider.modelName())
    }
    @get:WorkerThread
    public val totalRamSignal: TotalRamSignal by lazy {
        TotalRamSignal(memInfoProvider.totalRAM())
    }
    @get:WorkerThread
    public val totalInternalStorageSpaceSignal: TotalInternalStorageSpaceSignal by lazy {
        TotalInternalStorageSpaceSignal(memInfoProvider.totalInternalStorageSpace())
    }
    @get:WorkerThread
    public val procCpuInfoSignal: ProcCpuInfoSignal by lazy {
        ProcCpuInfoSignal(cpuInfoProvider.cpuInfo())
    }
    @get:WorkerThread
    public val procCpuInfoV2Signal: ProcCpuInfoV2Signal by lazy {
        ProcCpuInfoV2Signal(cpuInfoProvider.cpuInfoV2())
    }
    @get:WorkerThread
    public val sensorsSignal: SensorsSignal by lazy {
        SensorsSignal(sensorsDataSource.sensors())
    }
    @get:WorkerThread
    public val inputDevicesSignal: InputDevicesSignal by lazy {
        InputDevicesSignal(inputDeviceDataSource.getInputDeviceData())
    }
    @get:WorkerThread
    public val inputDevicesV2Signal: InputDevicesV2Signal by lazy {
        InputDevicesV2Signal(inputDeviceDataSource.getInputDeviceData())
    }
    @get:WorkerThread
    public val batteryHealthSignal: BatteryHealthSignal by lazy {
        BatteryHealthSignal(batteryInfoProvider.batteryHealth())
    }
    @get:WorkerThread
    public val batteryFullCapacitySignal: BatteryFullCapacitySignal by lazy {
        BatteryFullCapacitySignal(batteryInfoProvider.batteryTotalCapacity())
    }
    @get:WorkerThread
    public val cameraListSignal: CameraListSignal by lazy {
        CameraListSignal(cameraInfoProvider.getCameraInfo())
    }
    @get:WorkerThread
    public val glesVersionSignal: GlesVersionSignal by lazy {
        GlesVersionSignal(gpuInfoProvider.glesVersion())
    }
    @get:WorkerThread
    public val abiTypeSignal: AbiTypeSignal by lazy {
        AbiTypeSignal(cpuInfoProvider.abiType())
    }
    @get:WorkerThread
    public val coresCountSignal: CoresCountSignal by lazy {
        CoresCountSignal(cpuInfoProvider.coresCount())
    }
    @get:WorkerThread
    public val fingerprintSignal: FingerprintSignal by lazy {
        FingerprintSignal(osBuildInfoProvider.fingerprint())
    }
    @get:WorkerThread
    public val androidVersionSignal: AndroidVersionSignal by lazy {
        AndroidVersionSignal(osBuildInfoProvider.androidVersion())
    }
    @get:WorkerThread
    public val sdkVersionSignal: SdkVersionSignal by lazy {
        SdkVersionSignal(osBuildInfoProvider.sdkVersion())
    }
    @get:WorkerThread
    public val kernelVersionSignal: KernelVersionSignal by lazy {
        KernelVersionSignal(osBuildInfoProvider.kernelVersion())
    }
    @get:WorkerThread
    public val encryptionStatusSignal: EncryptionStatusSignal by lazy {
        EncryptionStatusSignal(deviceSecurityInfoProvider.encryptionStatus())
    }
    @get:WorkerThread
    public val codecListSignal: CodecListSignal by lazy {
        CodecListSignal(codecInfoProvider?.codecsList() ?: emptyList())
    }
    @get:WorkerThread
    public val securityProvidersSignal: SecurityProvidersSignal by lazy {
        SecurityProvidersSignal(deviceSecurityInfoProvider.securityProvidersData())
    }
    @get:WorkerThread
    public val applicationsListSignal: ApplicationsListSignal by lazy {
        ApplicationsListSignal(packageManagerDataSource.getApplicationsList())
    }
    @get:WorkerThread
    public val systemApplicationsListSignal: SystemApplicationsListSignal by lazy {
        SystemApplicationsListSignal(packageManagerDataSource.getSystemApplicationsList())
    }
    @get:WorkerThread
    public val adbEnabledSignal: AdbEnabledSignal by lazy {
        AdbEnabledSignal(settingsDataSource.adbEnabled())
    }
    @get:WorkerThread
    public val developmentSettingsEnabledSignal: DevelopmentSettingsEnabledSignal by lazy {
        DevelopmentSettingsEnabledSignal(settingsDataSource.developmentSettingsEnabled())
    }
    @get:WorkerThread
    public val httpProxySignal: HttpProxySignal by lazy {
        HttpProxySignal(settingsDataSource.httpProxy())
    }
    @get:WorkerThread
    public val transitionAnimationScaleSignal: TransitionAnimationScaleSignal by lazy {
        TransitionAnimationScaleSignal(settingsDataSource.transitionAnimationScale())
    }
    @get:WorkerThread
    public val windowAnimationScaleSignal: WindowAnimationScaleSignal by lazy {
        WindowAnimationScaleSignal(settingsDataSource.windowAnimationScale())
    }
    @get:WorkerThread
    public val dataRoamingEnabledSignal: DataRoamingEnabledSignal by lazy {
        DataRoamingEnabledSignal(settingsDataSource.dataRoamingEnabled())
    }
    @get:WorkerThread
    public val accessibilityEnabledSignal: AccessibilityEnabledSignal by lazy {
        AccessibilityEnabledSignal(settingsDataSource.accessibilityEnabled())
    }
    @get:WorkerThread
    public val defaultInputMethodSignal: DefaultInputMethodSignal by lazy {
        DefaultInputMethodSignal(settingsDataSource.defaultInputMethod())
    }
    @get:WorkerThread
    public val rttCallingModeSignal: RttCallingModeSignal by lazy {
        RttCallingModeSignal(settingsDataSource.rttCallingMode())
    }
    @get:WorkerThread
    public val touchExplorationEnabledSignal: TouchExplorationEnabledSignal by lazy {
        TouchExplorationEnabledSignal(settingsDataSource.touchExplorationEnabled())
    }
    @get:WorkerThread
    public val alarmAlertPathSignal: AlarmAlertPathSignal by lazy {
        AlarmAlertPathSignal(settingsDataSource.alarmAlertPath())
    }
    @get:WorkerThread
    public val dateFormatSignal: DateFormatSignal by lazy {
        DateFormatSignal(settingsDataSource.dateFormat())
    }
    @get:WorkerThread
    public val endButtonBehaviourSignal: EndButtonBehaviourSignal by lazy {
        EndButtonBehaviourSignal(settingsDataSource.endButtonBehaviour())
    }
    @get:WorkerThread
    public val fontScaleSignal: FontScaleSignal by lazy {
        FontScaleSignal(settingsDataSource.fontScale())
    }
    @get:WorkerThread
    public val screenOffTimeoutSignal: ScreenOffTimeoutSignal by lazy {
        ScreenOffTimeoutSignal(settingsDataSource.screenOffTimeout())
    }
    @get:WorkerThread
    public val textAutoReplaceEnabledSignal: TextAutoReplaceEnabledSignal by lazy {
        TextAutoReplaceEnabledSignal(settingsDataSource.textAutoReplaceEnable())
    }
    @get:WorkerThread
    public val textAutoPunctuateSignal: TextAutoPunctuateSignal by lazy {
        TextAutoPunctuateSignal(settingsDataSource.textAutoPunctuate())
    }
    @get:WorkerThread
    public val time12Or24Signal: Time12Or24Signal by lazy {
        Time12Or24Signal(settingsDataSource.time12Or24())
    }
    @get:WorkerThread
    public val isPinSecurityEnabledSignal: IsPinSecurityEnabledSignal by lazy {
        IsPinSecurityEnabledSignal(deviceSecurityInfoProvider.isPinSecurityEnabled())
    }
    @get:WorkerThread
    public val fingerprintSensorStatusSignal: FingerprintSensorStatusSignal by lazy {
        FingerprintSensorStatusSignal(fingerprintSensorInfoProvider.getStatus().stringDescription)
    }
    @get:WorkerThread
    public val ringtoneSourceSignal: RingtoneSourceSignal by lazy {
        RingtoneSourceSignal(devicePersonalizationInfoProvider.ringtoneSource())
    }
    @get:WorkerThread
    public val availableLocalesSignal: AvailableLocalesSignal by lazy {
        AvailableLocalesSignal(devicePersonalizationInfoProvider.availableLocales().toList())
    }
    @get:WorkerThread
    public val regionCountrySignal: RegionCountrySignal by lazy {
        RegionCountrySignal(devicePersonalizationInfoProvider.regionCountry())
    }
    @get:WorkerThread
    public val defaultLanguageSignal: DefaultLanguageSignal by lazy {
        DefaultLanguageSignal(devicePersonalizationInfoProvider.defaultLanguage())
    }
    @get:WorkerThread
    public val timezoneSignal: TimezoneSignal by lazy {
        TimezoneSignal(devicePersonalizationInfoProvider.timezone())
    }
}
