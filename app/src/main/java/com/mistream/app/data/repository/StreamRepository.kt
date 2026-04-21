package com.mistream.app.data.repository

import com.mistream.app.data.api.TorrentioApiService
import com.mistream.app.data.model.StreamItem
import com.mistream.app.di.RealDebridTokenHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamRepository @Inject constructor(
    private val api: TorrentioApiService
) {
    private val base = "https://torrentio.strem.fun"

    private fun buildConfig(): String {
        val token = RealDebridTokenHolder.token
        return if (token.isNotEmpty()) "realdebrid=$token" else ""
    }

    suspend fun getMovieStreams(imdbId: String): List<StreamItem> {
        val config = buildConfig()
        val url = if (config.isNotEmpty()) "$base/$config/stream/movie/$imdbId.json"
                  else "$base/stream/movie/$imdbId.json"
        return api.getMovieStreams(url).streams.sortedByDescending { it.sortPriority }
    }

    suspend fun getEpisodeStreams(imdbId: String, season: Int, episode: Int): List<StreamItem> {
        val config = buildConfig()
        val url = if (config.isNotEmpty()) "$base/$config/stream/series/$imdbId:$season:$episode.json"
                  else "$base/stream/series/$imdbId:$season:$episode.json"
        return api.getEpisodeStreams(url).streams.sortedByDescending { it.sortPriority }
    }
}
