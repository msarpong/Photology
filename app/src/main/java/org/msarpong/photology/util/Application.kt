package org.msarpong.photology.util

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.msarpong.photology.BuildConfig
import org.msarpong.photology.di.androidComponents
import org.msarpong.photology.di.appComponents
import org.msarpong.photology.di.retrofitModule
import org.msarpong.photology.di.viewModels

class Application : Application() {
    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
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
