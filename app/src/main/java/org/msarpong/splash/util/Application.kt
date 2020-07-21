package org.msarpong.splash.util

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.msarpong.splash.BuildConfig
import org.msarpong.splash.di.androidComponents
import org.msarpong.splash.di.appComponents
import org.msarpong.splash.di.retrofitModule
import org.msarpong.splash.di.viewModels

class Application : Application() {
    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        setupDI()
        instance = this
    }

    private fun setupDI() {
        startKoin {
            androidLogger()
            androidContext(this@Application)
            val appSetupModule = module { single { BuildConfig.DEBUG } }
            modules(
                listOf(
                    appSetupModule,
                    androidComponents,
                    appComponents,
                    viewModels,
                    retrofitModule
                )
            )
        }
    }
}