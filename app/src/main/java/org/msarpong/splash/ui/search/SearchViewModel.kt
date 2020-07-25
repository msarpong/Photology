package org.msarpong.splash.ui.search

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.msarpong.splash.service.Service

sealed class SearchEvent {
    object Load : SearchEvent()
}

sealed class SearchState {
    object InProgress : SearchState()
    // data class Success(val collection: Collection) : SearchState()
    data class Error(val error: Throwable) : SearchState()
}

class SearchViewModel(context: Context) : ViewModel() {
    private val service = Service()
    var state: MutableLiveData<SearchState> = MutableLiveData()

    init {
        state.value = SearchState.InProgress
    }

}