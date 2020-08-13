package org.msarpong.photology.ui.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.photology.service.Service
import org.msarpong.photology.service.ServiceReceiver
import org.msarpong.photology.service.ServiceResult
import org.msarpong.photology.service.mapping.detail_photo.DetailPhotoResponse

sealed class SplashEvent {
    data class Load(val query: String) : SplashEvent()

}

sealed class SplashState {
    object InProgress : SplashState()
    data class Success(val photoList: DetailPhotoResponse) : SplashState()
    data class Error(val error: Throwable) : SplashState()
}

class SplashViewModel(context: Context) : ViewModel() {
    private val service = Service()
    var state: MutableLiveData<SplashState> = MutableLiveData()

    init {
        state.value = SplashState.InProgress
    }

    fun send(event: SplashEvent) {
        when (event) {
            is SplashEvent.Load -> loadPhoto(event.query)
        }
    }

    private fun loadPhoto(query: String) {
        Log.d("SplashViewModel", "loadPhoto")
        try {
            service.getRandomPhoto(query, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.SuccessResultRandomPhoto -> state.value =
                            SplashState.Success(result.photoRandom)
                        is ServiceResult.Error -> state.value =
                            SplashState.Error(error = result.error)
                    }
                }

            })
        } catch (exception: Throwable) {
            state.value = SplashState.Error(exception)
        }
    }
}
