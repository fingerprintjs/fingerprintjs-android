package com.fingerprintjs.android.playground.fingerprinters_screen


import android.os.Parcelable
import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.SignalProviderType
import com.fingerprintjs.android.fingerprint.signal_providers.hardware.HardwareSignalProvider
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
        private val fingerprinter: Fingerprinter,
        private val fingerprinterVersion: Int,
        private val externalStorageDir: String?,
        state: Parcelable?
) : PlaygroundPresenter {

    private var view: PlaygroundView? = null
    private val itemConverter = FingerprintItemConverterImpl()

    private var items: List<FingerprinterItem>? = null
    private var csvFilePath: String? = null
    private var signalProvidersMask: Int = DEFAULT_FINGERPRINT_MASK

    init {
        val savedState = state as? State
        savedState?.let {
            items = savedState.items
            csvFilePath = savedState.csvFilePath
        }
    }

    override fun attachView(playgroundView: PlaygroundView) {
        view = playgroundView
        setupCustomFingerprint(playgroundView)
        view?.setFingerprinterVersion(fingerprinterVersion)
        fingerprinter.getDeviceId { deviceIdResult ->
            fingerprinter.getFingerprint { fingerprintResult ->
                val adapterItems = items ?: itemConverter.convert(
                        deviceIdResult,
                        fingerprintResult
                )

                view?.setFingerprintItems(
                        adapterItems
                )

                prepareCsvFile(fingerprintResult, deviceIdResult, adapterItems)
            }
        }
    }

    override fun detachView() {
        view?.setOnCustomFingerprintChangedListener(null)
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

    private fun prepareCsvFile(
            fingerprintResult: FingerprintResult,
            deviceIdResult: DeviceIdResult,
            items: List<FingerprinterItem>
    ) {
        if (csvFilePath != null) {
            return
        }

        fingerprintResult.getSignalProvider(HardwareSignalProvider::class.java)?.let {
            externalStorageDir?.let { externalStorageDir ->
                val csvFilePath = "$externalStorageDir/${it.rawData().manufacturerName}-${it.rawData().modelName}-${deviceIdResult.deviceId}.csv"
                this.csvFilePath = csvFilePath
                itemConverter.convertToCsvFile(csvFilePath, items)
            }
        }
    }

    private fun setupCustomFingerprint(view: PlaygroundView) {
        view.setOnCustomFingerprintChangedListener {
            signalProvidersMask = signalProvidersMask xor it

            fingerprinter.getFingerprint(signalProvidersMask) { fingerprintResult ->
                view.setCustomFingerprint(fingerprintResult.fingerprint)
            }
        }

        signalProvidersMask =
                SignalProviderType.HARDWARE or SignalProviderType.OS_BUILD or
                        SignalProviderType.DEVICE_STATE

        fingerprinter.getFingerprint(signalProvidersMask) { fingerprintResult ->
            view.setCustomFingerprint(
                    fingerprintResult.fingerprint,
                    listOf(
                            SignalProviderType.HARDWARE,
                            SignalProviderType.OS_BUILD,
                            SignalProviderType.DEVICE_STATE
                    )
            )
        }
    }
}

@Parcelize
private class State(
        val items: List<FingerprinterItem>?,
        val csvFilePath: String?
) : Parcelable