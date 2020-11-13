package com.fingerprintjs.android.playground.fingerprinters_screen


import android.os.Parcelable
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.Type
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprintItemConverterImpl
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprinterItem
import kotlinx.android.parcel.Parcelize


interface PlaygroundPresenter {
    fun attachView(playgroundView: PlaygroundView)
    fun detachView()
    fun onSaveState(): Parcelable
}

class PlaygroundPresenterImpl(
    private val fingerprintAgent: Fingerprinter,
    state: Parcelable?
) : PlaygroundPresenter {

    private var view: PlaygroundView? = null
    private val itemConverter = FingerprintItemConverterImpl()

    private var items: List<FingerprinterItem>? = null
    private var customFingerprintMask: Int = DEFAULT_FINGERPRINT_MASK

    init {
        val savedState = state as? State
        savedState?.let {
            items = savedState.items
        }
    }

    override fun attachView(playgroundView: PlaygroundView) {
        view = playgroundView
        setupCustomFingerprint(playgroundView)
        view?.setFingerprintItems(items ?: itemConverter.convert(fingerprintAgent))
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
            customFingerprintMask = customFingerprintMask xor it
            view.setCustomFingerprint(
                fingerprintAgent.fingerprint(
                    customFingerprintMask
                )
            )
        }

        customFingerprintMask =
            Type.HARDWARE or Type.OS_BUILD or
                    Type.DEVICE_STATE

        val customFingerprintValue = fingerprintAgent.fingerprint(
            customFingerprintMask
        )
        view.setCustomFingerprint(
            customFingerprintValue,
            listOf(
                Type.HARDWARE,
                Type.OS_BUILD,
                Type.DEVICE_STATE
            )
        )
    }
}

@Parcelize
private class State(
    val items: List<FingerprinterItem>?
) : Parcelable

private val DEFAULT_FINGERPRINT_MASK =
    (Type.HARDWARE or Type.OS_BUILD or Type.DEVICE_STATE)