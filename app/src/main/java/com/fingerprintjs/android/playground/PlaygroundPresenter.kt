package com.fingerprintjs.android.playground


import android.os.Parcelable
import com.fingerprintjs.android.fingerprint.FingerprintAndroidAgent
import kotlinx.android.parcel.Parcelize


interface PlaygroundPresenter {
    fun attachView(playgroundView: PlaygroundView)
    fun detachView()
    fun onSaveState(): Parcelable
}

class PlaygroundPresenterImpl(
    private val fingerprinter: FingerprintAndroidAgent,
    state: Parcelable?
) : PlaygroundPresenter {

    private var view: PlaygroundView? = null

    private var deviceId: String? = null
    private var hardwareFingerprint: String? = null
    private var osBuildFingerprint: String? = null
    private var installedAppsFingerprint: String? = null
    private var deviceStateFingerprint: String? = null

    init {
        val savedState = state as? State
        savedState?.let {
            deviceId = it.deviceId
            hardwareFingerprint = it.hardwareFingerprint
            osBuildFingerprint = it.osBuildFingerprint
            installedAppsFingerprint = it.installedAppsFingerprint
            deviceStateFingerprint = it.deviceStateFingerprint
        }
    }

    override fun attachView(playgroundView: PlaygroundView) {
        view = playgroundView
        view?.apply {
            setDeviceId(deviceId ?: fingerprinter.deviceId())
            setHardwareFingerprint(
                hardwareFingerprint ?: fingerprinter.hardwareFingerprinter().calculate()
            )
            setOsBuildFingerprint(
                osBuildFingerprint ?: fingerprinter.osBuildFingerprinter().calculate()
            )
            setInstalledAppsFingerprint(
                installedAppsFingerprint ?: fingerprinter.installedAppsFingerprinter().calculate()
            )
            setDeviceStateFingerprint(
                deviceStateFingerprint ?: fingerprinter.deviceStateFingerprinter().calculate()
            )
        }
    }

    override fun detachView() {
        view = null
    }

    override fun onSaveState(): Parcelable {
        return State(
            deviceId,
            hardwareFingerprint,
            osBuildFingerprint,
            installedAppsFingerprint,
            deviceStateFingerprint
        )
    }
}

@Parcelize
private class State(
    val deviceId: String?,
    val hardwareFingerprint: String?,
    val osBuildFingerprint: String?,
    val installedAppsFingerprint: String?,
    val deviceStateFingerprint: String?
) : Parcelable