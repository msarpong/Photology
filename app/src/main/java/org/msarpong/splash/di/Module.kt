package org.msarpong.splash.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.msarpong.splash.service.Service

val androidComponents = module {
    single { androidContext().resources }
}

val appComponents = module {
    single { Service() }
}
