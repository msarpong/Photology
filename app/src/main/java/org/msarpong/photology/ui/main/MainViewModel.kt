package org.msarpong.photology.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.photology.service.Service
import org.msarpong.photology.service.ServiceReceiver
import org.msarpong.photology.service.ServiceResult
import org.msarpong.photology.service.mapping.photos.PhotoResponse

sealed class MainEvent {
    object Load : MainEvent()
}

sealed class MainState {
    object InProgress : MainState()
    data class Success(val pictureList: PhotoResponse) : MainState()
    data class Error(val error: Throwable) : MainState()
}

class MainViewModel(context: Context) : ViewModel() {
    private val service = Service()
    var state: MutableLiveData<MainState> = MutableLiveData()

    init {
        state.value = MainState.InProgress
    }

    fun send(event: MainEvent) {
        when (event) {
            is MainEvent.Load -> loadContent()
        }
    }

    private fun loadContent() {
        Log.d("MainViewModel", "loadContent")
        try {
            service.getHome(object : ServiceReceiver {
                override fun receive(result: ServiceResult) {

                    when (result) {
                        is ServiceResult.Success -> state.value =
                            MainState.Success(
                                pictureList = result.pictureList
                            )

                        is ServiceResult.Error -> state.value =
                            MainState.Error(error = result.error)
                    }
                }
            })
        } catch (exception: Throwable) {
            state.value = MainState.Error(exception)
        }

    }
}
