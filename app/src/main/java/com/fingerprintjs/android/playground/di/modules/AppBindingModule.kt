package com.fingerprintjs.android.playground.di.modules

import android.app.Application
import android.content.Context
import com.fingerprintjs.android.playground.App
import dagger.Binds
import dagger.Module

@Module
interface AppBindingModule {

    @Binds
    fun bindAppToApplication(application: App): Application

    @Binds
    fun bindAppToContext(application: App): Context
}