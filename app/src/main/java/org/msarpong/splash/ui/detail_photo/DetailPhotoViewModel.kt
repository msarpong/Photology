package org.msarpong.splash.ui.detail_photo

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service
import org.msarpong.splash.service.ServiceReceiver
import org.msarpong.splash.service.ServiceResult
import org.msarpong.splash.service.mapping.detail_photo.DetailPhotoResponse
import org.msarpong.splash.service.mapping.like.LikePhotoResponse

sealed class DetailEvent {
    data class Load(val token: String, val detailId: String) : DetailEvent()
    data class LikePhoto(val code: String, val detailId: String) : DetailEvent()
    data class UnLikePhoto(val code: String, val detailId: String) : DetailEvent()
}

sealed class DetailState {
    object InProgress : DetailState()
    data class Error(val error: Throwable) : DetailState()
    data class Success(val pictureDetail: DetailPhotoResponse) : DetailState()
    data class SuccessLike(val likeResponse: LikePhotoResponse) : DetailState()
    data class SuccessUnLike(val likeResponse: LikePhotoResponse) : DetailState()
}

class DetailPhotoViewModel(context: Context) : ViewModel() {

    private val service = Service()

    var state: MutableLiveData<DetailState> = MutableLiveData()

    init {
        state.value = DetailState.InProgress
    }

    fun send(event: DetailEvent) {
        when (event) {
            is DetailEvent.Load -> loadContent(event.token, event.detailId)
            is DetailEvent.LikePhoto -> likePhotoDetail(event.code, event.detailId)
            is DetailEvent.UnLikePhoto -> unLikePhoto(event.code, event.detailId)
        }
    }

    private fun unLikePhoto(code: String, detailId: String) {
        Log.d("unlikePhoto", "$detailId $code")
        try {
            service.unLikePhoto(code, detailId, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.Error -> state.value =
                            DetailState.Error(error = result.error)
                        is ServiceResult.SuccessResultLike -> state.value =
                            DetailState.SuccessLike(likeResponse = result.like)
                    }
                }

            })
        } catch (exception: Throwable) {
            state.value = DetailState.Error(exception)
        }    }

    private fun likePhotoDetail(code: String, detailId: String) {
        Log.d("likePhoto", detailId)
        try {
            service.likePhoto(code, detailId, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.Error -> state.value =
                            DetailState.Error(error = result.error)
                        is ServiceResult.SuccessResultLike -> state.value =
                            DetailState.SuccessLike(likeResponse = result.like)
                    }
                }

            })
        } catch (exception: Throwable) {
            state.value = DetailState.Error(exception)
        }
    }

    private fun loadContent(token: String, detailId: String) {
        Log.d("loadContent", detailId)
        Log.d("getKey_loadContent", "authToken: $token - detailId: $detailId")

        state.value = DetailState.InProgress
        try {
            service.getDetail(token, detailId, object : ServiceReceiver {
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
