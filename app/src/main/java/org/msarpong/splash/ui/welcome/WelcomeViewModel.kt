package org.msarpong.splash.ui.welcome

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.AuthService
import org.msarpong.splash.service.AuthServiceReceiver
import org.msarpong.splash.service.AuthServiceResult
import org.msarpong.splash.service.mapping.auth.AuthResponse

sealed class WelcomeEvent {
    data class Load(val code: String) : WelcomeEvent()
}

sealed class WelcomeState {
    object InProgress : WelcomeState()
    data class Success(val authResult: AuthResponse) : WelcomeState()
    data class Error(val error: Throwable) : WelcomeState()
}

class WelcomeViewModel(context: Context) : ViewModel() {
    private val service = AuthService()
    var state: MutableLiveData<WelcomeState> = MutableLiveData()

    init {
        state.value = WelcomeState.InProgress
    }

    fun send(event: WelcomeEvent) {
        when (event) {
            is WelcomeEvent.Load -> loadContent(event.code)
        }
    }

    private fun loadContent(code: String) {
        try {
            Log.d("WelcomeViewModel", "loadContent: $code")
            service.authUser(code, object : AuthServiceReceiver {
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
