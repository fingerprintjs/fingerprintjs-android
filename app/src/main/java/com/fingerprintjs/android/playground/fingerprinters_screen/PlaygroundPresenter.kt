package com.fingerprintjs.android.playground.fingerprinters_screen


import android.os.Parcelable
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.signal_providers.device_id.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalGroupProvider
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprintItemConverterImpl
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprinterItem
import kotlinx.parcelize.Parcelize


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
        subscribeToView()

        fingerprinter.getFingerprint(stabilityLevel) { fingerprintResult ->
            updateView(fingerprintResult)
        }
    }

    override fun detachView() {
        unsubscribeFromView()
        view = null
    }

    override fun onSaveState(): Parcelable {
        return State(
            items,
            csvFilePath
        )
    }

    private fun subscribeToView() {
        view?.setOnStabilityChangedListener {
            stabilityLevel = it
            reloadFingerprinter(version, stabilityLevel)
        }

        view?.setOnVersionChangedListener {
            version = it
            reloadFingerprinter(version, stabilityLevel)
        }
    }

    private fun unsubscribeFromView() {
        view?.setOnStabilityChangedListener(null)
        view?.setOnVersionChangedListener(null)
    }

    override fun shareActionClicked(listener: (String) -> (Unit)) {
        csvFilePath?.let {
            listener.invoke(it)
        }
    }

    private fun reloadFingerprinter(version: Int, stabilityLevel: StabilityLevel) {
        fingerprinter = fingerprinterProvider.provideInstance(version)
        fingerprinter.getFingerprint(stabilityLevel) { fingerprintResult ->
            updateView(fingerprintResult, true)
        }
    }

    private fun updateView(
        fingerprintResult: FingerprintResult,
        needToConvert: Boolean = false
    ) {
        val adapterItems = if (!needToConvert && items != null) {
            items ?: itemConverter.convert(
                fingerprintResult,
                stabilityLevel,
                version
            )
        } else {
            itemConverter.convert(
                fingerprintResult,
                stabilityLevel,
                version
            )
        }

        view?.setFingerprint(fingerprintResult.fingerprint, version, stabilityLevel)
        view?.setFingerprintItems(
            adapterItems
        )

        prepareCsvFile(fingerprintResult, adapterItems)
    }

    private fun prepareCsvFile(
        fingerprintResult: FingerprintResult,
        items: List<FingerprinterItem>
    ) {
        if (csvFilePath != null) {
            return
        }

        val deviceId = fingerprintResult.getSignalProvider(DeviceIdProvider::class.java)?.fingerprint()

        fingerprintResult.getSignalProvider(HardwareSignalGroupProvider::class.java)?.let {
            externalStorageDir?.let { externalStorageDir ->
                val csvFilePath =
                    "$externalStorageDir/${it.rawData().manufacturerName}-${it.rawData().modelName}-$deviceId.csv"
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