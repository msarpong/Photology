package org.msarpong.splash.ui.detail_photo

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service
import org.msarpong.splash.service.ServiceReceiver
import org.msarpong.splash.service.ServiceResult
import org.msarpong.splash.service.mapping.detail_photo.DetailPhotoResponse

sealed class DetailEvent {
    data class Load(val detailId: String) : DetailEvent()
}

sealed class DetailState {
    object InProgress : DetailState()
    data class Error(val error: Throwable) : DetailState()
    data class Success(val pictureDetail: DetailPhotoResponse) : DetailState()
}

class DetailPhotoViewModel(context: Context) : ViewModel() {

    private val service = Service()

    var state: MutableLiveData<DetailState> = MutableLiveData()

    init {
        state.value = DetailState.InProgress
    }

    fun send(event: DetailEvent) {
        when (event) {
            is DetailEvent.Load -> loadContent(event.detailId)
        }
    }

    private fun loadContent(detailId: String) {
        Log.d("loadContent", detailId)

        state.value = DetailState.InProgress
        try {

            service.getDetail(detailId, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.Error -> state.value =
                            DetailState.Error(error = result.error)
                        is ServiceResult.Detail -> state.value =
                            DetailState.Success(pictureDetail = result.pictureList)
                    }
                }
            })

        } catch (exception: Throwable) {
            state.value = DetailState.Error(exception)
        }
    }

}
