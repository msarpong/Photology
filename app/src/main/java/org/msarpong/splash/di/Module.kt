package org.msarpong.splash.di

import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.msarpong.splash.service.Service
import org.msarpong.splash.ui.collections.CollectionViewModel
import org.msarpong.splash.ui.detail_photo.DetailPhotoViewModel
import org.msarpong.splash.ui.main.MainViewModel
import org.msarpong.splash.ui.profile.ProfileViewModel

val androidComponents = module {
    single { androidContext().resources }
}

val appComponents = module {
    single { Service() }
}

val viewModels = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailPhotoViewModel(get()) }
    viewModel { CollectionViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
