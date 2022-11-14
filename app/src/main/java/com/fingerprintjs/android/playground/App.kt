package com.fingerprintjs.android.playground

import android.app.Application
import com.fingerprintjs.android.playground.di.AppComponent
import com.fingerprintjs.android.playground.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .app(this)
            .build()
    }
}