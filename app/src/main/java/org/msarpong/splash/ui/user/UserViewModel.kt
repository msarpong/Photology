package org.msarpong.splash.ui.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service
import org.msarpong.splash.service.ServiceReceiver
import org.msarpong.splash.service.ServiceResult
import org.msarpong.splash.service.mapping.auth.user.UserResponse

sealed class UserEvent {
    data class Load(val code: String) : UserEvent()
}

sealed class UserState {
    object InProgress : UserState()
    data class Success(val user: UserResponse) : UserState()
    data class Error(val error: Throwable) : UserState()
}

class UserViewModel(context: Context) : ViewModel() {
    private val service = Service()
    var state: MutableLiveData<UserState> = MutableLiveData()

    init {
        state.value = UserState.InProgress
    }

    fun send(event: UserEvent) {
        when (event) {
            is UserEvent.Load -> loadContent(event.code)
        }
    }

    private fun loadContent(code: String) {
        Log.d("UserViewModel", "loadContent")
        try {
            service.getCurrentUser(code, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {

                    when (result) {
                        is ServiceResult.SuccessUser -> state.value =
                            UserState.Success(user = result.user)
                        is ServiceResult.Error -> state.value =
                            UserState.Error(error = result.error)
                    }
                }
            })
        } catch (exception: Throwable) {
            state.value = UserState.Error(exception)
        }


    }

}