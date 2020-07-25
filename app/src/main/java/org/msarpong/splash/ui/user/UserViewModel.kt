package org.msarpong.splash.ui.user

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service

sealed class UserEvent {
    object Load : UserEvent()
}

sealed class UserState {
    object InProgress : UserState()
    // data class Success(val collection: Collection) : SearchState()
    data class Error(val error: Throwable) : UserState()
}

class UserViewModel(context: Context) : ViewModel() {
    private val service = Service()
    var state: MutableLiveData<UserState> = MutableLiveData()

    init {
        state.value = UserState.InProgress
    }

}