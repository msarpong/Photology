package org.msarpong.splash.di

import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.msarpong.splash.service.Service
import org.msarpong.splash.ui.detail_photo.DetailPhotoViewModel
import org.msarpong.splash.ui.main.MainViewModel

val androidComponents = module {
    single { androidContext().resources }
}

val appComponents = module {
    single { Service() }
}

val viewModels = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailPhotoViewModel(get()) }
}

