package org.msarpong.splash.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidComponents = module {
    single { androidContext().resources }
}
