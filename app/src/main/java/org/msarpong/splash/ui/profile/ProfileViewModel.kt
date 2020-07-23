package org.msarpong.splash.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service
import org.msarpong.splash.service.ServiceReceiver
import org.msarpong.splash.service.ServiceResult
import org.msarpong.splash.service.mapping.profile.Profile
import org.msarpong.splash.ui.detail_photo.DetailState

sealed class ProfileEvent {
    data class Load(val username: String) : ProfileEvent()
}

sealed class ProfileState {
    object InProgress : ProfileState()
    data class Success(val pictureList: Profile) : ProfileState()
    data class Error(val error: Throwable) : ProfileState()
}

class ProfileViewModel(context: Context) : ViewModel() {

    private val service = Service()

    var state: MutableLiveData<ProfileState> = MutableLiveData()

    init {
        state.value = ProfileState.InProgress
    }

    fun send(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Load -> loadContent(event.username)
        }
    }

    private fun loadContent(username: String) {
        Log.d("loadContent", username)
        state.value = ProfileState.InProgress


        try {
            service.getProfile(username, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.Error -> state.value =
                            ProfileState.Error(error = result.error)
                        is ServiceResult.SuccessProfile -> state.value =
                            ProfileState.Success(pictureList = result.profile)
                    }
                }
            })
        } catch (exception: Throwable) {
            state.value = ProfileState.Error(exception)
        }
    }
}