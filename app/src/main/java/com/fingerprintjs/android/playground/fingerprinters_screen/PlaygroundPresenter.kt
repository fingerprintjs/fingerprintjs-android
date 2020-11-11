package com.fingerprintjs.android.playground.fingerprinters_screen


import android.os.Parcelable
import com.fingerprintjs.android.fingerprint.FingerprintAndroidAgent
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprintItemConverterImpl
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprinterItem
import kotlinx.android.parcel.Parcelize


interface PlaygroundPresenter {
    fun attachView(playgroundView: PlaygroundView)
    fun detachView()
    fun onSaveState(): Parcelable
}

class PlaygroundPresenterImpl(
    private val fingerprintAgent: FingerprintAndroidAgent,
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
                fingerprintAgent.getFingerprint(
                    customFingerprintMask
                )
            )
        }

        customFingerprintMask =
            FingerprintAndroidAgent.HARDWARE or FingerprintAndroidAgent.OS_BUILD or
                    FingerprintAndroidAgent.DEVICE_STATE

        val customFingerprintValue = fingerprintAgent.getFingerprint(
            customFingerprintMask
        )
        view.setCustomFingerprint(
            customFingerprintValue,
            listOf(
                FingerprintAndroidAgent.HARDWARE,
                FingerprintAndroidAgent.OS_BUILD,
                FingerprintAndroidAgent.DEVICE_STATE
            )
        )
    }
}

@Parcelize
private class State(
    val items: List<FingerprinterItem>?
) : Parcelable

private val DEFAULT_FINGERPRINT_MASK =
    (FingerprintAndroidAgent.HARDWARE or FingerprintAndroidAgent.OS_BUILD or FingerprintAndroidAgent.DEVICE_STATE)