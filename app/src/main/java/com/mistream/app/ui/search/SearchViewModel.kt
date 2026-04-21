package com.mistream.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mistream.app.data.model.TmdbMovie
import com.mistream.app.data.repository.TmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val tmdbRepository: TmdbRepository
) : ViewModel() {

    private val _results = MutableStateFlow<List<TmdbMovie>>(emptyList())
    val results: StateFlow<List<TmdbMovie>> = _results

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var hasSearched = false
        private set

    private var searchJob: Job? = null

    fun search(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            _isLoading.value = true
            hasSearched = true
            runCatching {
                _results.value = tmdbRepository.searchMulti(query)
            }.onFailure {
                _results.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun clearResults() {
        searchJob?.cancel()
        _results.value = emptyList()
        hasSearched = false
    }
}
