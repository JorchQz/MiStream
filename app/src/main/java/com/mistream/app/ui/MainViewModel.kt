package com.mistream.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mistream.app.data.model.TmdbMovie
import com.mistream.app.data.repository.PreferencesRepository
import com.mistream.app.data.repository.TmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tmdbRepository: TmdbRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _trending = MutableStateFlow<List<TmdbMovie>>(emptyList())
    val trending: StateFlow<List<TmdbMovie>> = _trending

    private val _popularMovies = MutableStateFlow<List<TmdbMovie>>(emptyList())
    val popularMovies: StateFlow<List<TmdbMovie>> = _popularMovies

    private val _popularSeries = MutableStateFlow<List<TmdbMovie>>(emptyList())
    val popularSeries: StateFlow<List<TmdbMovie>> = _popularSeries

    init {
        viewModelScope.launch { preferencesRepository.loadTokens() }
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch { runCatching { _trending.value = tmdbRepository.getTrending() } }
        viewModelScope.launch { runCatching { _popularMovies.value = tmdbRepository.getPopularMovies() } }
        viewModelScope.launch { runCatching { _popularSeries.value = tmdbRepository.getPopularSeries() } }
    }
}
