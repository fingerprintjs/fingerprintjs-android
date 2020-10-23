package com.fingerprintjs.android.playground

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import com.fingerprintjs.android.fingerprint.FingerprintAndroidAgentFactory

class MainActivity : AppCompatActivity() {


    private lateinit var presenter: PlaygroundPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init(savedInstanceState)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = super.onCreateView(name, context, attrs) ?: return null
        presenter.attachView(
            PlaygroundViewImpl(
                view
            )
        )
        return view
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putParcelable(PLAYGROUND_PRESENTER_STATE_KEY, presenter.onSaveState())
    }


    private fun init(state: Bundle?) {
        val fingerprintAndroidAgent = FingerprintAndroidAgentFactory.getInitializedInstance(this)
        val presenterState: Parcelable? = state?.getParcelable(PLAYGROUND_PRESENTER_STATE_KEY)
        presenter = PlaygroundPresenterImpl(
            fingerprintAndroidAgent, presenterState
        )
    }
}

private const val PLAYGROUND_PRESENTER_STATE_KEY = "PlaygroundPresenterState"