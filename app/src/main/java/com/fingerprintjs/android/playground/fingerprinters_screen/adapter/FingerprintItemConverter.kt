package com.fingerprintjs.android.playground.fingerprinters_screen.adapter


import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.installed_apps.InstalledAppsSignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.os_build.OsBuildSignalGroupProvider
import java.io.File
import java.util.LinkedList


interface FingerprintItemConverter {
    fun convert(
        deviceIdResult: DeviceIdResult,
        fingerprintResult: FingerprintResult
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
        fingerprintResult: FingerprintResult
    ): List<FingerprinterItem> {
        val list = LinkedList<FingerprinterItem>()
        list.add(prepareDeviceIdItem(deviceIdResult))
        fingerprintResult.getSignalProvider(HardwareSignalGroupProvider::class.java)?.let {
            list.add(prepareHardwareFingerprinterItem(it))
        }

        fingerprintResult.getSignalProvider(OsBuildSignalGroupProvider::class.java)?.let {
            list.add(prepareOsBuildFingerprint(it))
        }

        fingerprintResult.getSignalProvider(InstalledAppsSignalGroupProvider::class.java)?.let {
            list.add(prepareInstalledAppsFingerprinter(it))
        }

        fingerprintResult.getSignalProvider(DeviceStateSignalGroupProvider::class.java)?.let {
            list.add(prepareDeviceStateFingerprint(it))
        }
        return list
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

    private fun prepareHardwareFingerprinterItem(hardwareSignalProvider: HardwareSignalGroupProvider): FingerprinterItem {
        val deviceModelDescription = FingerprintSectionDescription(
            "Device name",
            listOf(
                Pair("Device manufacturer", hardwareSignalProvider.rawData().manufacturerName),
                Pair("Device model", hardwareSignalProvider.rawData().modelName)
            )
        )

        val memoryDescription =
            FingerprintSectionDescription(
                "Memory info",
                listOf(
                    Pair(
                        "Total RAM in bytes",
                        hardwareSignalProvider.rawData().totalRAM.toString()
                    ),
                    Pair(
                        "Total internal storage in bytes",
                        hardwareSignalProvider.rawData().totalInternalStorageSpace.toString()
                    )
                )
            )

        val cpuInfoList = LinkedList<Pair<String, String>>()

        cpuInfoList.addAll(
            listOf(
                Pair("Cores count", hardwareSignalProvider.rawData().coresCount.toString()),
                Pair("ABI type", hardwareSignalProvider.rawData().abiType),
                Pair("GL ES version", hardwareSignalProvider.rawData().glesVersion),
            )
        )

        cpuInfoList.addAll(hardwareSignalProvider.rawData().procCpuInfo.entries.map {
            Pair(it.key, it.value)
        })


        val cpuInfoDescription =
            FingerprintSectionDescription(
                "CPU info",
                cpuInfoList
            )

        val sensorsDescription = FingerprintSectionDescription(
            "Sensors",
            hardwareSignalProvider.rawData().sensors.map {
                Pair(it.sensorName, it.vendorName)
            }
        )

        val inputDevicesDescription = FingerprintSectionDescription(
            "Input devices",
            hardwareSignalProvider.rawData().inputDevices.map {
                Pair(it.vendor, it.name)
            }
        )

        val batteryInfoDescription = FingerprintSectionDescription(
            "Battery Info",
            listOf(
                Pair("Battery full capacity", hardwareSignalProvider.rawData().batteryFullCapacity),
                Pair("Battery health", hardwareSignalProvider.rawData().batteryHealth)
            )
        )

        val cameraInfoDescription = FingerprintSectionDescription(
            "Cameras",
            hardwareSignalProvider.rawData().cameraList.map {
                Pair(it.cameraName, "${it.cameraType}-${it.cameraOrientation}")
            }
        )

        return FingerprinterItem(
            "hardware",
            "Hardware Fingerprint",
            hardwareSignalProvider.fingerprint(),
            listOf(
                deviceModelDescription,
                memoryDescription,
                cpuInfoDescription,
                sensorsDescription,
                inputDevicesDescription,
                batteryInfoDescription,
                cameraInfoDescription
            )
        )
    }

    private fun prepareInstalledAppsFingerprinter(installedAppsSignalProvider: InstalledAppsSignalGroupProvider): FingerprinterItem {
        val installedAppsFingerprintDescription = FingerprintSectionDescription(
            "Installed apps",
            installedAppsSignalProvider.rawData().applicationsNamesList.mapIndexed { index, app ->
                Pair(index.toString(), app.packageName)
            }
        )

        val systemAppsFingerprintDescription = FingerprintSectionDescription(
            "System apps",
            installedAppsSignalProvider.rawData().systemApplicationsList.mapIndexed { index, app ->
                Pair(index.toString(), app.packageName)
            }
        )
        return FingerprinterItem(
            "apps",
            "Installed apps fingerprint",
            installedAppsSignalProvider.fingerprint(),
            listOf(
                installedAppsFingerprintDescription,
                systemAppsFingerprintDescription
            )
        )
    }

    private fun prepareOsBuildFingerprint(osBuildSignalProvider: OsBuildSignalGroupProvider): FingerprinterItem {
        val osBuildDescription = FingerprintSectionDescription(
            "OS Build fingerprint",
            listOf(
                Pair("Build fingerprint", osBuildSignalProvider.rawData().fingerprint),
                Pair("Android version", osBuildSignalProvider.rawData().androidVersion),
                Pair("SDK version", osBuildSignalProvider.rawData().sdkVersion),
                Pair("Kernel version", osBuildSignalProvider.rawData().kernelVersion),
                Pair("Encryption status", osBuildSignalProvider.rawData().encryptionStatus)
            )
        )

        val codecListInfo = FingerprintSectionDescription(
            "Codecs",
            osBuildSignalProvider.rawData().codecList.map {
                val sb = StringBuilder()

                it.capabilities.forEach { capability ->
                    sb.append("$capability  ")
                }
                Pair(it.name, sb.toString())
            }
        )

        val securityProviders = FingerprintSectionDescription(
            "Security providers",
            osBuildSignalProvider.rawData().securityProvidersData
        )

        return FingerprinterItem(
            "osBuild",
            "OS Build fingerprint",
            osBuildSignalProvider.fingerprint(),
            listOf(
                osBuildDescription,
                codecListInfo,
                securityProviders
            )
        )
    }

    private fun prepareDeviceStateFingerprint(
        deviceStateSignalProvider: DeviceStateSignalGroupProvider
    ): FingerprinterItem {
        val deviceStateDescription = FingerprintSectionDescription(
            "Device state fingerprint params",
            listOf(
                Pair(
                    "Accessibility enabled",
                    deviceStateSignalProvider.rawData().accessibilityEnabled
                ),
                Pair("ADB enabled", deviceStateSignalProvider.rawData().adbEnabled),
                Pair(
                    "Development settings enabled",
                    deviceStateSignalProvider.rawData().developmentSettingsEnabled
                ),
                Pair("HTTP proxy", deviceStateSignalProvider.rawData().httpProxy),
                Pair(
                    "Transition animation scale",
                    deviceStateSignalProvider.rawData().transitionAnimationScale
                ),
                Pair(
                    "Window animation scale",
                    deviceStateSignalProvider.rawData().windowAnimationScale
                ),
                Pair(
                    "Data roaming enabled",
                    deviceStateSignalProvider.rawData().dataRoamingEnabled
                ),
                Pair(
                    "Default input method",
                    deviceStateSignalProvider.rawData().defaultInputMethod
                ),
                Pair("RTT calling mode", deviceStateSignalProvider.rawData().rttCallingMode),
                Pair(
                    "Touch exploration enabled",
                    deviceStateSignalProvider.rawData().touchExplorationEnabled
                ),
                Pair("Alarm alert path", deviceStateSignalProvider.rawData().alarmAlertPath),
                Pair("Date format", deviceStateSignalProvider.rawData().dateFormat),
                Pair(
                    "End button behaviour",
                    deviceStateSignalProvider.rawData().endButtonBehaviour
                ),
                Pair("Font scale", deviceStateSignalProvider.rawData().fontScale),
                Pair("Screen off timeout", deviceStateSignalProvider.rawData().screenOffTimeout),
                Pair(
                    "Text autoreplace enable",
                    deviceStateSignalProvider.rawData().textAutoReplaceEnable
                ),
                Pair("Text auto punctuate", deviceStateSignalProvider.rawData().textAutoPunctuate),
                Pair("Time 12 or 24", deviceStateSignalProvider.rawData().time12Or24),
                Pair(
                    "is PIN security enabled",
                    deviceStateSignalProvider.rawData().isPinSecurityEnabled.toString()
                ),
                Pair(
                    "Fingerprint sensor status",
                    deviceStateSignalProvider.rawData().fingerprintSensorStatus
                ),
                Pair("Ringtone source", deviceStateSignalProvider.rawData().ringtoneSource),
                Pair("Region country", deviceStateSignalProvider.rawData().regionCountry),
                Pair("Timezone", deviceStateSignalProvider.rawData().timezone),
                Pair(
                    "Available locales",
                    deviceStateSignalProvider.rawData().availableLocales.toString()
                )
            )
        )
        return FingerprinterItem(
            "deviceState",
            "Device state fingerprint",
            deviceStateSignalProvider.fingerprint(),
            listOf(
                deviceStateDescription
            )
        )
    }
}

private const val DELIMITER = "|"
private const val CSV_FILE_HEADER = "GROUP${DELIMITER}PARAMETER_NAME${DELIMITER}PARAMETER_VALUE\n"