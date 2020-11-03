package com.fingerprintjs.android.playground


import android.app.Activity
import android.widget.TextView


interface PlaygroundView {
    fun setDeviceId(deviceId: String)
    fun setHardwareFingerprint(hardwareFingerprint: String)
    fun setOsBuildFingerprint(osBuildFingerprint: String)
    fun setInstalledAppsFingerprint(installedAppsFingerprint: String)
    fun setDeviceStateFingerprint(deviceStateFingerprint: String)
}

class PlaygroundViewImpl(
    activity: Activity
) : PlaygroundView {
    private val deviceIdView: TextView = activity.findViewById(R.id.device_id)
    private val hardwareFingerprintView: TextView =
        activity.findViewById(R.id.hardware_fingerprint_text_view)
    private val osBuildFingerprintView: TextView =
        activity.findViewById(R.id.os_build_fingerprint_text_view)
    private val installedAppsFingerprintView: TextView =
        activity.findViewById(R.id.installed_applications_fingerprint_text_view)
    private val deviceStateFingerprintView: TextView =
        activity.findViewById(R.id.device_state_fingerprint_text_view)

    override fun setDeviceId(deviceId: String) {
        deviceIdView.text = deviceId
    }

    override fun setHardwareFingerprint(hardwareFingerprint: String) {
        hardwareFingerprintView.text = hardwareFingerprint
    }

    override fun setOsBuildFingerprint(osBuildFingerprint: String) {
        osBuildFingerprintView.text = osBuildFingerprint
    }

    override fun setInstalledAppsFingerprint(installedAppsFingerprint: String) {
        installedAppsFingerprintView.text = installedAppsFingerprint
    }

    override fun setDeviceStateFingerprint(deviceStateFingerprint: String) {
        deviceStateFingerprintView.text = deviceStateFingerprint
    }
}