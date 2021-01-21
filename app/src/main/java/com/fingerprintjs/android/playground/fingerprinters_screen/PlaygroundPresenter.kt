package com.fingerprintjs.android.playground.fingerprinters_screen


import android.os.Parcelable
import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprintItemConverterImpl
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprinterItem
import kotlinx.android.parcel.Parcelize


interface PlaygroundPresenter {
    fun shareActionClicked(listener: (String) -> (Unit))
    fun attachView(playgroundView: PlaygroundView)
    fun detachView()
    fun onSaveState(): Parcelable
}

class PlaygroundPresenterImpl(
    private val fingerprinterProvider: FingerprinterProvider,
    fingerprinterVersion: Int,
    private val externalStorageDir: String?,
    state: Parcelable?
) : PlaygroundPresenter {

    private var view: PlaygroundView? = null
    private val itemConverter = FingerprintItemConverterImpl()

    private var fingerprinter: Fingerprinter

    private var items: List<FingerprinterItem>? = null
    private var stabilityLevel = StabilityLevel.OPTIMAL
    private var version = fingerprinterVersion

    private var csvFilePath: String? = null

    init {
        fingerprinter = fingerprinterProvider.provideInstance(version)
        val savedState = state as? State
        savedState?.let {
            items = savedState.items
            csvFilePath = savedState.csvFilePath
        }
    }

    override fun attachView(playgroundView: PlaygroundView) {
        view = playgroundView

        fingerprinter.getDeviceId { deviceIdResult ->
            fingerprinter.getFingerprint(stabilityLevel) { fingerprintResult ->
                updateView(fingerprintResult, deviceIdResult)
            }
        }

        view?.setOnStabilityChangedListener {
            stabilityLevel = it
            reloadFingerprinter(version, stabilityLevel)
        }

        view?.setOnVersionChangedListener {
            version = it
            reloadFingerprinter(version, stabilityLevel)
        }
    }

    override fun detachView() {
        view?.setOnStabilityChangedListener(null)
        view?.setOnVersionChangedListener(null)
        view = null
    }

    override fun onSaveState(): Parcelable {
        return State(
            items,
            csvFilePath
        )
    }

    override fun shareActionClicked(listener: (String) -> (Unit)) {
        csvFilePath?.let {
            listener.invoke(it)
        }
    }

    private fun reloadFingerprinter(version: Int, stabilityLevel: StabilityLevel) {
        fingerprinter = fingerprinterProvider.provideInstance(version)
        fingerprinter.getDeviceId { deviceIdResult ->
            fingerprinter.getFingerprint(stabilityLevel) { fingerprintResult ->
                updateView(fingerprintResult, deviceIdResult)
            }
        }
    }

    private fun updateView(fingerprintResult: FingerprintResult, deviceIdResult: DeviceIdResult) {
        val adapterItems = items ?: itemConverter.convert(
            deviceIdResult,
            fingerprintResult,
            stabilityLevel,
            version
        )

        view?.setFingerprint(fingerprintResult.fingerprint, version, stabilityLevel)
        view?.setFingerprintItems(
            adapterItems
        )

        prepareCsvFile(fingerprintResult, deviceIdResult, adapterItems)
    }

    private fun prepareCsvFile(
        fingerprintResult: FingerprintResult,
        deviceIdResult: DeviceIdResult,
        items: List<FingerprinterItem>
    ) {
        if (csvFilePath != null) {
            return
        }

        fingerprintResult.getSignalProvider(HardwareSignalGroupProvider::class.java)?.let {
            externalStorageDir?.let { externalStorageDir ->
                val csvFilePath =
                    "$externalStorageDir/${it.rawData().manufacturerName}-${it.rawData().modelName}-${deviceIdResult.deviceId}.csv"
                this.csvFilePath = csvFilePath
                itemConverter.convertToCsvFile(csvFilePath, items)
            }
        }
    }
}

@Parcelize
private class State(
    val items: List<FingerprinterItem>?,
    val csvFilePath: String?
) : Parcelable