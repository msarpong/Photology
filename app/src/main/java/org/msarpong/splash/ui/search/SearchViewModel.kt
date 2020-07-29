package org.msarpong.splash.ui.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service
import org.msarpong.splash.service.ServiceReceiver
import org.msarpong.splash.service.ServiceResult
import org.msarpong.splash.service.mapping.search.SearchResponse
import org.msarpong.splash.service.mapping.search.collections.SearchCollectionResponse
import org.msarpong.splash.service.mapping.search.users.SearchUserResponse

sealed class SearchEvent {
    data class Load(val term: String) : SearchEvent()
    data class LoadUser(val term: String) : SearchEvent()
    data class LoadCollection(val term: String) : SearchEvent()
}

sealed class SearchState {
    object InProgress : SearchState()
    data class Success(val result: SearchResponse) : SearchState()
    data class SuccessUser(val result: SearchUserResponse) : SearchState()
    data class SuccessCollection(val result: SearchCollectionResponse) : SearchState()
    data class Error(val error: Throwable) : SearchState()
}

class SearchViewModel(context: Context) : ViewModel() {
    private val service = Service()
    var state: MutableLiveData<SearchState> = MutableLiveData()

    init {
        state.value = SearchState.InProgress
    }

    fun send(event: SearchEvent) {
        when (event) {
            is SearchEvent.Load -> loadContent(event.term)
            is SearchEvent.LoadUser -> loadResultUser(event.term)
            is SearchEvent.LoadCollection -> loadResultCollection(event.term)
        }
    }

    private fun loadResultUser(term: String) {
        Log.d("SearchViewModel", "loadResultUser")
        try {
            service.searchUsers(term, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.SuccessResultUser -> state.value =
                            SearchState.SuccessUser(result = result.search)
                        is ServiceResult.Error -> state.value =
                            SearchState.Error(error = result.error)
                    }
                }

            })
        } catch (exception: Throwable) {
            state.value = SearchState.Error(exception)
        }
    }

    private fun loadResultCollection(term: String) {
        Log.d("SearchViewModel", "loadResultCollection")
        try {
            service.searchCollections(term, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.SuccessResultCollection -> state.value =
                            SearchState.SuccessCollection(result = result.search)

                        is ServiceResult.Error -> state.value =
                            SearchState.Error(error = result.error)
                    }
                }

            })
        } catch (exception: Throwable) {
            state.value = SearchState.Error(exception)
        }
    }

    private fun loadContent(query: String) {
        Log.d("SearchViewModel", "loadContent: $query")
        try {
            service.searchPhotos(query, object : ServiceReceiver {
                override fun receive(result: ServiceResult) {
                    when (result) {
                        is ServiceResult.SuccessSearch -> state.value =
                            SearchState.Success(result = result.search)
                        is ServiceResult.Error -> state.value =
                            SearchState.Error(error = result.error)
                    }
                }

            })
        } catch (exception: Throwable) {
            state.value = SearchState.Error(exception)
        }
    }
}