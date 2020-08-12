package org.msarpong.splash.ui.collections

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service
import org.msarpong.splash.service.ServiceReceiver
import org.msarpong.splash.service.ServiceResult
import org.msarpong.splash.service.mapping.collection.Collection
import org.msarpong.splash.service.mapping.photos.PhotoResponse

sealed class CollectionEvent {
    object Load : CollectionEvent()
    data class LoadPhoto(val detailId: String) : CollectionEvent()
}

sealed class CollectionState {
    object InProgress : CollectionState()
    data class Success(val collection: Collection) : CollectionState()
    data class SuccessPhoto(val photoList: PhotoResponse) : CollectionState()
    data class Error(val error: Throwable) : CollectionState()
}

class CollectionViewModel(context: Context) : ViewModel() {
    private val service = Service()
    var state: MutableLiveData<CollectionState> = MutableLiveData()

    init {
        state.value = CollectionState.InProgress
    }

    fun send(event: CollectionEvent) {
        when (event) {
            is CollectionEvent.Load -> loadContent()
            is CollectionEvent.LoadPhoto -> loadPhotoCollection(event.detailId)
        }
    }

    private fun loadPhotoCollection(detailId: String) {
        Log.d("CollectionViewModel", "loadPhotoCollection")
        try {
            service.getCollectionPhoto(detailId, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.SuccessResultCollectionPhoto -> state.value =
                            CollectionState.SuccessPhoto(photoList = result.photoCollection)
                        is ServiceResult.Error ->
                            state.value = CollectionState.Error(error = result.error)
                    }
                }
            })
        } catch (exception: Throwable) {
            state.value = CollectionState.Error(exception)
        }
    }

    private fun loadContent() {
        Log.d("CollectionViewModel", "loadContent")
        try {
            service.getCollection(object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.SuccessCollection ->
                            state.value =
                                CollectionState.Success(collection = result.collectionList)
                        is ServiceResult.Error ->
                            state.value = CollectionState.Error(error = result.error)
                    }
                }
            })
        } catch (exception: Throwable) {
            state.value = CollectionState.Error(exception)
        }

    }

}
