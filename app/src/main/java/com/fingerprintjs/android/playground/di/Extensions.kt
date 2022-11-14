package com.fingerprintjs.android.playground.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fingerprintjs.android.playground.App
import com.fingerprintjs.android.playground.di.AppComponent

@Composable
fun getAppComponent(): AppComponent {
    return (LocalContext.current.applicationContext as App).appComponent
}

fun Context.getAppComponent(): AppComponent {
    return (this.applicationContext as App).appComponent
}
