package com.fingerprintjs.android.playground.fingerprinters_screen


import android.os.Parcelable
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.SignalProviderType
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprintItemConverterImpl
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprinterItem
import kotlinx.android.parcel.Parcelize


interface PlaygroundPresenter {
    fun attachView(playgroundView: PlaygroundView)
    fun detachView()
    fun onSaveState(): Parcelable
}

class PlaygroundPresenterImpl(
    private val fingerprinter: Fingerprinter,
    state: Parcelable?
) : PlaygroundPresenter {

    private var view: PlaygroundView? = null
    private val itemConverter = FingerprintItemConverterImpl()

    private var items: List<FingerprinterItem>? = null
    private var signalProvidersMask: Int = DEFAULT_FINGERPRINT_MASK

    init {
        val savedState = state as? State
        savedState?.let {
            items = savedState.items
        }
    }

    override fun attachView(playgroundView: PlaygroundView) {
        view = playgroundView
        setupCustomFingerprint(playgroundView)
        fingerprinter.getDeviceId { deviceIdResult ->
            fingerprinter.getFingerprint { fingerprintResult ->
                view?.setFingerprintItems(
                    items ?: itemConverter.convert(
                        deviceIdResult,
                        fingerprintResult
                    )
                )
            }
        }
    }

    override fun detachView() {
        view?.setOnCustomFingerprintChangedListener(null)
        view = null
    }

    override fun onSaveState(): Parcelable {
        return State(
            items
        )
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
    val items: List<FingerprinterItem>?
) : Parcelable
