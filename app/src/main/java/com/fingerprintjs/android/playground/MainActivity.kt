package com.fingerprintjs.android.playground


import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.fingerprint.FingerprintAndroidAgentFactory
import com.fingerprintjs.android.playground.fingerprinters_screen.PlaygroundPresenter
import com.fingerprintjs.android.playground.fingerprinters_screen.PlaygroundPresenterImpl
import com.fingerprintjs.android.playground.fingerprinters_screen.PlaygroundViewImpl


class MainActivity : AppCompatActivity() {

    private lateinit var presenter: PlaygroundPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init(savedInstanceState)
        presenter.attachView(
            PlaygroundViewImpl(
                this
            )
        )
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putParcelable(PLAYGROUND_PRESENTER_STATE_KEY, presenter.onSaveState())
    }

    private fun init(state: Bundle?) {
        val fingerprintAndroidAgent = FingerprintAndroidAgentFactory.getInitializedInstance(applicationContext)
        val presenterState: Parcelable? = state?.getParcelable(PLAYGROUND_PRESENTER_STATE_KEY)
        presenter =
            PlaygroundPresenterImpl(
                fingerprintAndroidAgent, presenterState
            )
    }
}

private const val PLAYGROUND_PRESENTER_STATE_KEY = "PlaygroundPresenterState"