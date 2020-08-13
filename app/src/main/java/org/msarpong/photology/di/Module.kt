package org.msarpong.photology.di

import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.msarpong.photology.service.Service
import org.msarpong.photology.ui.collections.CollectionViewModel
import org.msarpong.photology.ui.detail_photo.DetailPhotoViewModel
import org.msarpong.photology.ui.following.FollowingViewModel
import org.msarpong.photology.ui.main.MainViewModel
import org.msarpong.photology.ui.profile.ProfileViewModel
import org.msarpong.photology.ui.search.SearchViewModel
import org.msarpong.photology.ui.settings.SettingsViewModel
import org.msarpong.photology.ui.splash.SplashViewModel
import org.msarpong.photology.ui.user.UserViewModel
import org.msarpong.photology.ui.welcome.WelcomeViewModel
import org.msarpong.photology.util.sharedpreferences.KeyValueStorageFactory

val androidComponents = module {
    single { androidContext().resources }
    single { KeyValueStorageFactory.build(context = androidContext(), name = "splash_prefs") }
}

val appComponents = module {
    single { Service() }
}

val viewModels = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailPhotoViewModel(get()) }
    viewModel { CollectionViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { WelcomeViewModel(get()) }
    viewModel { FollowingViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { SplashViewModel(get()) }
}
