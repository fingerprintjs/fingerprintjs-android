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

    init {
        val savedState = state as? State
        savedState?.let {
            deviceId = it.deviceId
            hardwareFingerprint = it.hardwareFingerprint
            osBuildFingerprint = it.osBuildFingerprint
        }
    }



    override fun attachView(playgroundView: PlaygroundView) {
        view = playgroundView
        view?.setDeviceId(deviceId ?: fingerprinter.deviceId())
        view?.setHardwareFingerprint(hardwareFingerprint ?: fingerprinter.hardwareFingerprint())
        view?.setOsBuildFingerprint(osBuildFingerprint ?: fingerprinter.osBuildFingerprint())
    }

    override fun detachView() {
        view = null
    }

    override fun onSaveState(): Parcelable {
        return State(
            deviceId,
            hardwareFingerprint,
            osBuildFingerprint
        )
    }
}

@Parcelize
private class State(
    val deviceId: String?,
    val hardwareFingerprint: String?,
    val osBuildFingerprint: String?
) : Parcelable