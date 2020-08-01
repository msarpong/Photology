package org.msarpong.splash.ui.welcome

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.*
import org.msarpong.splash.service.mapping.auth.AuthResponse
import org.msarpong.splash.service.mapping.auth.user.UserResponse

sealed class WelcomeEvent {
    data class Load(val code: String) : WelcomeEvent()
    data class  LoadMe(val token: String) : WelcomeEvent()
}

sealed class WelcomeState {
    object InProgress : WelcomeState()
    data class Success(val authResult: AuthResponse) : WelcomeState()
    data class SuccessUser(val userResult: UserResponse) : WelcomeState()
    data class Error(val error: Throwable) : WelcomeState()
}

class WelcomeViewModel(context: Context) : ViewModel() {
    private val authService = AuthService()
    private val service = Service()
    var state: MutableLiveData<WelcomeState> = MutableLiveData()

    init {
        state.value = WelcomeState.InProgress
    }

    fun send(event: WelcomeEvent) {
        when (event) {
            is WelcomeEvent.Load -> loadContent(event.code)
            is WelcomeEvent.LoadMe -> loadMyUser(event.token)
        }
    }

    private fun loadMyUser(token: String) {
        try {
            Log.d("WelcomeViewModel", "loadMyUser")
            service.getCurrentUser(token, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.Error -> state.value =
                            WelcomeState.Error(error = result.error)
                        is ServiceResult.SuccessUser -> state.value =
                            WelcomeState.SuccessUser(userResult = result.user)
                    }
                }

            })
        } catch (exception: Throwable) {
            state.value = WelcomeState.Error(exception)
        }

    }

    private fun loadContent(code: String) {
        try {
            Log.d("WelcomeViewModel", "loadContent: $code")
            authService.authUser(code, object : AuthServiceReceiver {
                override fun receive(result: AuthServiceResult) {
                    when (result) {
                        is AuthServiceResult.Error -> state.value =
                            WelcomeState.Error(error = result.error)
                        is AuthServiceResult.Success -> state.value =
                            WelcomeState.Success(authResult = result.authResult)
                    }
                }
            })

        } catch (exception: Throwable) {
            state.value = WelcomeState.Error(exception)
        }

    }
}
