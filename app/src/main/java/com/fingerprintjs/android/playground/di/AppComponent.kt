package com.fingerprintjs.android.playground.di

import com.fingerprintjs.android.playground.ui.screens.home.HomeViewModel
import com.fingerprintjs.android.playground.App
import com.fingerprintjs.android.pro.webviewplayground.di.AppScope
import com.fingerprintjs.android.playground.di.modules.AppBindingModule
import com.fingerprintjs.android.playground.di.modules.AppModule
import com.fingerprintjs.android.playground.ui.screens.main.MainViewModel
import dagger.BindsInstance
import dagger.Component


@AppScope
@Component(
    modules = [
        AppBindingModule::class,
        AppModule::class,
    ]
)
interface AppComponent {

    val mainViewModel: MainViewModel
    val homeViewModel: HomeViewModel

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun app(application: App): Builder
    }
}
