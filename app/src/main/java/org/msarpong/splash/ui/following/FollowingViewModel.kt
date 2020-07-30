package org.msarpong.splash.ui.following

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service
import org.msarpong.splash.service.ServiceReceiver
import org.msarpong.splash.service.ServiceResult
import org.msarpong.splash.service.mapping.following.FollowingResponse
import org.msarpong.splash.service.mapping.photos.PhotoResponse

sealed class FollowingEvent {
    data class LoadUser(val username: String) : FollowingEvent()
    data class LoadPhoto(val username: String) : FollowingEvent()
}

sealed class FollowingState {
    object InProgress : FollowingState()
    data class SuccessUser(val userList: FollowingResponse) : FollowingState()
    data class SuccessPhoto(val photoList: PhotoResponse) : FollowingState()
    data class Error(val error: Throwable) : FollowingState()
}

class FollowingViewModel(context: Context) : ViewModel() {
    private val service = Service()
    var state: MutableLiveData<FollowingState> = MutableLiveData()

    init {
        state.value = FollowingState.InProgress
    }

    fun send(event: FollowingEvent) {
        when (event) {
            is FollowingEvent.LoadUser -> loadUser(event.username)
            is FollowingEvent.LoadPhoto -> loadPhoto(event.username)
        }
    }

    private fun loadPhoto(username: String) {
    }

    private fun loadUser(username: String) {
        Log.d("FollowingViewModel", "loadUser")
        try {
            service.getFollowing(username, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.SuccessResultFollowing -> state.value =
                            FollowingState.SuccessUser(userList = result.following)
                        is ServiceResult.Error -> state.value =
                            FollowingState.Error(error = result.error)
                    }
                }

            })
        } catch (exception: Throwable) {
            state.value = FollowingState.Error(exception)
        }
    }
}