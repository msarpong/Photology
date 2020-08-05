package org.msarpong.splash.ui.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service
import org.msarpong.splash.service.ServiceReceiver
import org.msarpong.splash.service.ServiceResult
import org.msarpong.splash.service.mapping.auth.user.UserResponse

sealed class SettingEvent {
    data class Load(val code: String) : SettingEvent()
}

sealed class SettingState {
    object InProgress : SettingState()
    data class Success(val user: UserResponse) : SettingState()
    data class Error(val error: Throwable) : SettingState()
}

class SettingsViewModel(context: Context) : ViewModel() {
    private val service = Service()
    var state: MutableLiveData<SettingState> = MutableLiveData()

    init {
        state.value = SettingState.InProgress
    }

    fun send(event: SettingEvent) {
        when (event) {
            is SettingEvent.Load -> loadContent(event.code)
        }
    }

    private fun loadContent(code: String) {
        Log.d("SettingsViewModel", "loadContent")
        try {
            service.getCurrentUser(code, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {

                    when (result) {
                        is ServiceResult.SuccessUser -> state.value =
                            SettingState.Success(user = result.user)
                        is ServiceResult.Error -> state.value =
                            SettingState.Error(error = result.error)
                    }
                }
            })
        } catch (exception: Throwable) {
            state.value = SettingState.Error(exception)
        }
    }
}
