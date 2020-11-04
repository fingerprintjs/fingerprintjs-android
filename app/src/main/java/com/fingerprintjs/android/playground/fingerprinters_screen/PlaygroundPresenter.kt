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

    init {
        val savedState = state as? State
        savedState?.let {
            items = savedState.items
        }
    }

    override fun attachView(playgroundView: PlaygroundView) {
        view = playgroundView
        view?.setFingerprintItems(items ?: itemConverter.convert(fingerprintAgent))
    }

    override fun detachView() {
        view = null
    }

    override fun onSaveState(): Parcelable {
        return State(
            items
        )
    }
}

@Parcelize
private class State(
    val items: List<FingerprinterItem>?
) : Parcelable