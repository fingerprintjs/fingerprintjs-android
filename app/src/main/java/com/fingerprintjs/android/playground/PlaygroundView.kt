package com.fingerprintjs.android.playground


import android.view.View
import android.widget.TextView


interface PlaygroundView {
    fun setDeviceId(deviceId: String)
    fun setHardwareFingerprint(hardwareFingerprint: String)
    fun setOsBuildFingerprint(osBuildFingerprint: String)
}

class PlaygroundViewImpl(
    rootView: View
) : PlaygroundView {
    private val deviceIdView: TextView = rootView.findViewById(R.id.device_id)
    private val hardwareFingerprintView: TextView =
        rootView.findViewById(R.id.hardware_fingerprint_text_view)
    private val osBuildFingerprintView: TextView =
        rootView.findViewById(R.id.os_build_fingerprint_text_view)

    override fun setDeviceId(deviceId: String) {
        deviceIdView.text = deviceId
    }

    override fun setHardwareFingerprint(hardwareFingerprint: String) {
        hardwareFingerprintView.text = hardwareFingerprint
    }

    override fun setOsBuildFingerprint(osBuildFingerprint: String) {
        osBuildFingerprintView.text = osBuildFingerprint
    }
}