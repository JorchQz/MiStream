package com.mistream.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mistream.app.data.model.StreamItem
import com.mistream.app.data.model.TmdbMovieDetail
import com.mistream.app.data.repository.RealDebridRepository
import com.mistream.app.data.repository.StreamRepository
import com.mistream.app.data.repository.TmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val tmdbRepository: TmdbRepository,
    private val streamRepository: StreamRepository,
    private val realDebridRepository: RealDebridRepository
) : ViewModel() {

    private val _detail = MutableStateFlow<TmdbMovieDetail?>(null)
    val detail: StateFlow<TmdbMovieDetail?> = _detail

    private val _streams = MutableStateFlow<List<StreamItem>>(emptyList())
    val streams: StateFlow<List<StreamItem>> = _streams

    private val _isLoadingStreams = MutableStateFlow(false)
    val isLoadingStreams: StateFlow<Boolean> = _isLoadingStreams

    private val _streamStatus = MutableStateFlow("")
    val streamStatus: StateFlow<String> = _streamStatus

    private val _resolvedUrl = MutableSharedFlow<String>()
    val resolvedUrl: SharedFlow<String> = _resolvedUrl

    fun loadDetail(id: Int, mediaType: String) {
        viewModelScope.launch {
            runCatching {
                val detail = if (mediaType == "movie") tmdbRepository.getMovieDetail(id)
                             else tmdbRepository.getTvDetail(id)
                _detail.value = detail
                loadStreams(id, mediaType)
            }.onFailure {
                _streamStatus.value = "Error al cargar detalle: ${it.message}"
            }
        }
    }

    private suspend fun loadStreams(id: Int, mediaType: String) {
        _isLoadingStreams.value = true
        _streamStatus.value = "Buscando streams..."
        runCatching {
            val imdbId = tmdbRepository.getImdbId(id, mediaType == "movie")
            if (imdbId != null) {
                val streams = if (mediaType == "movie") streamRepository.getMovieStreams(imdbId)
                              else streamRepository.getEpisodeStreams(imdbId, 1, 1)
                _streams.value = streams
                _streamStatus.value = if (streams.isEmpty()) "No se encontraron streams" else ""
            } else {
                _streamStatus.value = "No se pudo obtener el ID de IMDb"
            }
        }.onFailure {
            _streamStatus.value = "Error al buscar streams: ${it.message}"
        }
        _isLoadingStreams.value = false
    }

    fun resolveStream(stream: StreamItem) {
        viewModelScope.launch {
            _streamStatus.value = "Procesando con Real-Debrid..."
            runCatching {
                val url = realDebridRepository.resolveStream(stream)
                if (url != null) {
                    _resolvedUrl.emit(url)
                    _streamStatus.value = ""
                } else {
                    _streamStatus.value = "No se pudo resolver el stream"
                }
            }.onFailure {
                _streamStatus.value = "Error: ${it.message}"
            }
        }
    }
}
