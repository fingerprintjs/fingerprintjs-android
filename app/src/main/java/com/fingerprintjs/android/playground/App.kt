package com.fingerprintjs.android.playground

import android.app.Application
import android.os.StrictMode
import com.fingerprintjs.android.playground.di.AppComponent
import com.fingerprintjs.android.playground.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        setupStrictMode()
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .app(this)
            .build()
    }

    private fun setupStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build())
        }
    }
}
