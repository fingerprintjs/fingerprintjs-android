package com.fingerprintjs.android.playground.fingerprinters_screen.adapter


import com.fingerprintjs.android.fingerprint.FingerprintAndroidAgent
import com.fingerprintjs.android.fingerprint.device_id_providers.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.device_state.DeviceStateFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.hardware.HardwareFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.installed_apps.InstalledAppsFingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinters.os_build_fingerprint.OsBuildFingerprinter
import java.util.LinkedList


interface FingerprintItemConverter {
    fun convert(fingerprintAgent: FingerprintAndroidAgent): List<FingerprinterItem>
}


class FingerprintItemConverterImpl : FingerprintItemConverter {
    override fun convert(fingerprintAgent: FingerprintAndroidAgent): List<FingerprinterItem> {
        val list = LinkedList<FingerprinterItem>()
        list.add(prepareDeviceIdItem(fingerprintAgent.deviceIdProvider()))
        list.add(prepareHardwareFingerprinterItem(fingerprintAgent.hardwareFingerprinter()))
        list.add(prepareOsBuildFingerprint(fingerprintAgent.osBuildFingerprinter()))
        list.add(prepareInstalledAppsFingerprinter(fingerprintAgent.installedAppsFingerprinter()))
        list.add(prepareDeviceStateFingerprint(fingerprintAgent.deviceStateFingerprinter()))
        return list
    }

    private fun prepareDeviceIdItem(deviceIdProvider: DeviceIdProvider): FingerprinterItem {
        val deviceIdDescription = FingerprintSectionDescription(
            "Device ID",
            listOf(
                Pair("GSF ID", deviceIdProvider.getGsfId() ?: ""),
                Pair("Android ID", deviceIdProvider.getAndroidId())
            )
        )

        return FingerprinterItem(
            "deviceId",
            "Device IDs",
            deviceIdProvider.getDeviceId(),
            listOf(
                deviceIdDescription
            )
        )
    }

    private fun prepareHardwareFingerprinterItem(hardwareFingerprinter: HardwareFingerprinter): FingerprinterItem {
        val deviceModelDescription = FingerprintSectionDescription(
            "Device name",
            listOf(
                Pair("Device manufacturer", hardwareFingerprinter.rawData().manufacturerName),
                Pair("Device model", hardwareFingerprinter.rawData().modelName)
            )
        )

        val memoryDescription =
            FingerprintSectionDescription(
                "Memory info",
                listOf(
                    Pair(
                        "Total RAM in bytes",
                        hardwareFingerprinter.rawData().totalRAM.toString()
                    ),
                    Pair(
                        "Total internal storage in bytes",
                        hardwareFingerprinter.rawData().totalInternalStorageSpace.toString()
                    )
                )
            )
        val cpuInfoDescription =
            FingerprintSectionDescription(
                "CPU info",
                hardwareFingerprinter.rawData().procCpuInfo.entries.map {
                    Pair(it.key, it.value)
                })

        val sensorsDescription = FingerprintSectionDescription(
            "Sensors",
            hardwareFingerprinter.rawData().sensors.map {
                Pair(it.sensorName, it.vendorName)
            }
        )

        val inputDevicesDescription = FingerprintSectionDescription(
            "Input devices",
            hardwareFingerprinter.rawData().inputDevices.map {
                Pair(it.vendor, it.name)
            }
        )


        return FingerprinterItem(
            "hardware",
            "Hardware Fingerprint",
            hardwareFingerprinter.calculate(),
            listOf(
                deviceModelDescription,
                memoryDescription,
                cpuInfoDescription,
                sensorsDescription,
                inputDevicesDescription
            )
        )
    }

    private fun prepareInstalledAppsFingerprinter(installedAppsFingerprinter: InstalledAppsFingerprinter): FingerprinterItem {
        val installedAppsFingerprintDescription = FingerprintSectionDescription(
            "Installed apps",
            installedAppsFingerprinter.rawData().applicationsNamesList.mapIndexed { index, app ->
                Pair(index.toString(), app.packageName)
            }

        )
        return FingerprinterItem(
            "apps",
            "Installed applications fingerprint",
            installedAppsFingerprinter.calculate(),
            listOf(
                installedAppsFingerprintDescription
            )
        )
    }

    private fun prepareOsBuildFingerprint(osBuildFingerprinter: OsBuildFingerprinter): FingerprinterItem {
        val osBuildDescription = FingerprintSectionDescription(
            "OS Build fingerprint",
            listOf(Pair("Build fingerprint", osBuildFingerprinter.rawData().fingerprint))
        )

        return FingerprinterItem(
            "osBuild",
            "OS Build fingerprint",
            osBuildFingerprinter.calculate(),
            listOf(
                osBuildDescription
            )
        )
    }

    private fun prepareDeviceStateFingerprint(
        deviceStateFingerprinter: DeviceStateFingerprinter
    ): FingerprinterItem {
        val deviceStateDescription = FingerprintSectionDescription(
            "Device state fingerprint params",
            listOf(
                Pair(
                    "Accessibility enabled",
                    deviceStateFingerprinter.rawData().accessibilityEnabled
                ),
                Pair("ADB enabled", deviceStateFingerprinter.rawData().adbEnabled),
                Pair(
                    "Development settings enabled",
                    deviceStateFingerprinter.rawData().developmentSettingsEnabled
                ),
                Pair("HTTP proxy", deviceStateFingerprinter.rawData().httpProxy),
                Pair(
                    "Transition animation scale",
                    deviceStateFingerprinter.rawData().transitionAnimationScale
                ),
                Pair(
                    "Window animation scale",
                    deviceStateFingerprinter.rawData().windowAnimationScale
                ),
                Pair("Data roaming enabled", deviceStateFingerprinter.rawData().dataRoamingEnabled),
                Pair("Default input method", deviceStateFingerprinter.rawData().defaultInputMethod),
                Pair("RTT calling mode", deviceStateFingerprinter.rawData().rttCallingMode),
                Pair(
                    "Touch exploration enabled",
                    deviceStateFingerprinter.rawData().touchExplorationEnabled
                ),
                Pair("Alarm alert path", deviceStateFingerprinter.rawData().alarmAlertPath),
                Pair("Date format", deviceStateFingerprinter.rawData().dateFormat),
                Pair("End button behaviour", deviceStateFingerprinter.rawData().endButtonBehaviour),
                Pair("Font scale", deviceStateFingerprinter.rawData().fontScale),
                Pair("Screen off timeout", deviceStateFingerprinter.rawData().screenOffTimeout),
                Pair(
                    "Text autoreplace enable",
                    deviceStateFingerprinter.rawData().textAutoReplaceEnable
                ),
                Pair("Text auto punctuate", deviceStateFingerprinter.rawData().textAutoPunctuate),
                Pair("Time 12 or 24", deviceStateFingerprinter.rawData().time12Or24),
                Pair(
                    "is PIN security enabled",
                    deviceStateFingerprinter.rawData().isPinSecurityEnabled.toString()
                ),
                Pair(
                    "Fingerprint sensor status",
                    deviceStateFingerprinter.rawData().fingerprintSensorStatus
                ),
                Pair("Ringtone source", deviceStateFingerprinter.rawData().ringtoneSource)
            )
        )
        return FingerprinterItem(
            "deviceState",
            "Device state fingerprint",
            deviceStateFingerprinter.calculate(),
            listOf(
                deviceStateDescription
            )
        )
    }
}