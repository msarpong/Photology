package org.msarpong.splash.ui.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service
import org.msarpong.splash.service.ServiceReceiver
import org.msarpong.splash.service.ServiceResult
import org.msarpong.splash.service.mapping.search.SearchResponse

sealed class SearchEvent {
    data class Load(val term: String) : SearchEvent()
}

sealed class SearchState {
    object InProgress : SearchState()
    data class Success(val result: SearchResponse) : SearchState()
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
            is SearchEvent.Load -> loadContent("photos", event.term)
        }
    }

    private fun loadContent(search_type: String, query: String) {
        Log.d("SearchViewModel", "loadContent")
        try {
            service.getSearchQuery(search_type, query, object : ServiceReceiver {
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