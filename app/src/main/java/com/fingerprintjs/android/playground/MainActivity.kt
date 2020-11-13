package com.fingerprintjs.android.playground


import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.playground.R.layout
import com.fingerprintjs.android.playground.fingerprinters_screen.AboutDialog
import com.fingerprintjs.android.playground.fingerprinters_screen.PlaygroundPresenter
import com.fingerprintjs.android.playground.fingerprinters_screen.PlaygroundPresenterImpl
import com.fingerprintjs.android.playground.fingerprinters_screen.PlaygroundViewImpl


class MainActivity : AppCompatActivity() {

    private lateinit var presenter: PlaygroundPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

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
        val fingerprintAndroidAgent =
            FingerprinterFactory.getInitializedInstance(applicationContext)
        val presenterState: Parcelable? = state?.getParcelable(PLAYGROUND_PRESENTER_STATE_KEY)
        presenter =
            PlaygroundPresenterImpl(
                fingerprintAndroidAgent, presenterState
            )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_about) {
            AboutDialog().show(this)
        }
        return true
    }
}

private const val PLAYGROUND_PRESENTER_STATE_KEY = "PlaygroundPresenterState"