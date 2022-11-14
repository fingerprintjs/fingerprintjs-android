package com.fingerprintjs.android.playground.di.modules

import android.content.Context
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.pro.webviewplayground.di.AppScope
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class AppModule {

    @Reusable
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @AppScope
    fun provideFingerprinter(context: Context): Fingerprinter {
        return FingerprinterFactory.create(context)
    }
}
