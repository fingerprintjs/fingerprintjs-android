package com.fingerprintjs.android.playground.fingerprinters_screen.adapter


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.info_providers.CameraInfo
import com.fingerprintjs.android.fingerprint.info_providers.InputDeviceData
import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo
import com.fingerprintjs.android.fingerprint.info_providers.PackageInfo
import com.fingerprintjs.android.fingerprint.info_providers.SensorData
import com.fingerprintjs.android.fingerprint.signal_providers.Signal
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import java.io.File
import java.util.LinkedList


interface FingerprintItemConverter {
    fun convert(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult,
        stabilityLevel: StabilityLevel,
        version: Int
    ): List<FingerprinterItem>

    fun convertToCsvFile(filePath: String, items: List<FingerprinterItem>)
}

class FingerprintItemConverterImpl : FingerprintItemConverter {
    override fun convertToCsvFile(filePath: String, items: List<FingerprinterItem>) {
        val csvSb = StringBuilder()
        csvSb.append(CSV_FILE_HEADER)

        items.forEach {
            it.description.forEach { item ->
                item.fields.forEach { field ->
                    csvSb
                        .append(item.name)
                        .append(DELIMITER)
                        .append(field.first)
                        .append(DELIMITER)
                        .append(field.second)
                        .append("\n")
                }
            }
        }

        val file = File(filePath)

        if (file.exists()) {
            file.delete()
        }

        try {
            file.writeText(csvSb.toString())
        } catch (e: Exception) {

        }
    }

    override fun convert(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult,
        stabilityLevel: StabilityLevel,
        version: Int
    ): List<FingerprinterItem> {
        val list = LinkedList<FingerprinterItem>()
        list.add(prepareDeviceIdItem(deviceIdResult))
        fingerprintResult.getSignalProvider(HardwareSignalGroupProvider::class.java)?.let {
            list.add(prepareHardwareFingerprinterItem(it, stabilityLevel, version))
        }

        fingerprintResult.getSignalProvider(OsBuildSignalGroupProvider::class.java)?.let {
            list.add(prepareOsBuildFingerprint(it, stabilityLevel, version))
        }

        fingerprintResult.getSignalProvider(InstalledAppsSignalGroupProvider::class.java)?.let {
            list.add(prepareInstalledAppsFingerprinter(it, stabilityLevel, version))
        }

        fingerprintResult.getSignalProvider(DeviceStateSignalGroupProvider::class.java)?.let {
            list.add(prepareDeviceStateFingerprint(it, stabilityLevel, version))
        }
        return list.filter {
            it.description.isNotEmpty()
        }
    }

    private fun prepareDeviceIdItem(deviceIdResult: DeviceIdResult): FingerprinterItem {
        val deviceIdDescription = FingerprintSectionDescription(
            "Device ID",
            listOf(
                Pair("GSF ID", deviceIdResult.gsfId ?: ""),
                Pair("Android ID", deviceIdResult.androidId)
            )
        )

        return FingerprinterItem(
            "deviceId",
            "Device IDs",
            deviceIdResult.deviceId,
            listOf(
                deviceIdDescription
            )
        )
    }

    private fun prepareHardwareFingerprinterItem(
        hardwareSignalProvider: HardwareSignalGroupProvider,
        stabilityLevel: StabilityLevel,
        version: Int
    ) = prepareSignalGroupProviderSection(
        hardwareSignalProvider,
        "hardware",
        "Hardware Fingerprint",
        "Hardware signals",
        stabilityLevel,
        version
    )

    private fun prepareInstalledAppsFingerprinter(
        installedAppsSignalProvider: InstalledAppsSignalGroupProvider,
        stabilityLevel: StabilityLevel,
        version: Int
    ) = prepareSignalGroupProviderSection(
        installedAppsSignalProvider,
        "apps",
        "Installed apps fingerprint",
        "",
        stabilityLevel,
        version
    )

    private fun prepareOsBuildFingerprint(
        osBuildSignalProvider: OsBuildSignalGroupProvider,
        stabilityLevel: StabilityLevel,
        version: Int
    ) = prepareSignalGroupProviderSection(
        osBuildSignalProvider,
        "osBuild",
        "OS Build fingerprint",
        "OS Build signals",
        stabilityLevel,
        version
    )

    private fun prepareDeviceStateFingerprint(
        deviceStateSignalProvider: DeviceStateSignalGroupProvider,
        stabilityLevel: StabilityLevel,
        version: Int
    ) = prepareSignalGroupProviderSection(
        deviceStateSignalProvider,
        "deviceState",
        "Device state fingerprint",
        "Device state signals",
        stabilityLevel,
        version
    )


    private fun prepareSignalGroupProviderSection(
        signalProvider: SignalGroupProvider<*>,
        sectionId: String,
        title: String,
        stringSectionTitle: String,
        stabilityLevel: StabilityLevel,
        version: Int
    ): FingerprinterItem {
        return FingerprinterItem(
            sectionId,
            title,
            signalProvider.fingerprint(stabilityLevel),
            convertSignalToFingerprintSectionDescription(
                signalProvider.rawData().signals(version, stabilityLevel),
                stringSectionTitle
            )
        )
    }

    private fun convertSignalToFingerprintSectionDescription(
        signals: List<Signal<*>>,
        stringSectionTitle: String = ""
    ): List<FingerprintSectionDescription> {
        val sections = LinkedList<FingerprintSectionDescription>()

        val stringSignals = signals.filter {
            (it.value is String) or (it.value is Int) or (it.value is Long) or (it.value is Boolean)
        }.map {
            Pair(it.displayName, it.toString())
        }

        val stringSection = FingerprintSectionDescription(stringSectionTitle, stringSignals)

        val notStringSections: List<FingerprintSectionDescription> = signals
            .filter {
                (it.value !is String) and (it.value !is Int) and (it.value !is Long) and (it.value !is Boolean)
            }.map { signal ->
                when (val value = signal.value) {
                    is List<*> -> {
                        if (value.isNotEmpty() and (value[0] is String) or (value[0] is PackageInfo)) {
                            val sb = StringBuilder()

                            value.forEach {
                                sb.append(it.toString()).append("\n")
                            }

                            sb.append("Total: ${value.size} ${signal.displayName}\n")
                            FingerprintSectionDescription(
                                signal.displayName,
                                listOf(Pair(signal.displayName, sb.toString()))
                            )
                        } else {
                            val listItems = value.mapIndexed() { index, item ->
                                when (item) {
                                    is MediaCodecInfo -> {
                                        val sb = StringBuilder()
                                        item.capabilities.forEach { capability ->
                                            sb.append("$capability  ")
                                        }
                                        Pair(item.name, sb.toString())
                                    }
                                    is InputDeviceData -> {
                                        Pair(item.vendor, item.name)
                                    }
                                    is SensorData -> {
                                        Pair(item.sensorName, item.vendorName)
                                    }
                                    is CameraInfo -> {
                                        Pair(
                                            item.cameraName,
                                            "${item.cameraType}-${item.cameraOrientation}"
                                        )
                                    }
                                    is Pair<*, *> -> Pair(
                                        item.first.toString(),
                                        item.second.toString()
                                    )
                                    else -> {
                                        Pair(index.toString(), item.toString())
                                    }
                                }
                            }
                            FingerprintSectionDescription(signal.displayName, listItems)
                        }
                    }
                    is Map<*, *> -> {
                        val list = value.entries.map { entry ->
                            Pair(entry.key.toString(), entry.value.toString())
                        }
                        FingerprintSectionDescription(signal.displayName, list)
                    }
                    else -> {
                        FingerprintSectionDescription(
                            signal.displayName,
                            listOf(
                                Pair(signal.displayName, signal.toString())
                            )
                        )
                    }
                }
            }

        if (stringSection.fields.isNotEmpty()) {
            sections.add(stringSection)
        }

        sections.addAll(notStringSections)

        return sections
    }
}

private const val DELIMITER = "|"
private const val CSV_FILE_HEADER = "GROUP${DELIMITER}PARAMETER_NAME${DELIMITER}PARAMETER_VALUE\n"