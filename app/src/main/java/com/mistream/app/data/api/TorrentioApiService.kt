package com.mistream.app.data.api

import com.mistream.app.data.model.TorrentioResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface TorrentioApiService {
    @GET
    suspend fun getMovieStreams(@Url url: String): TorrentioResponse

    @GET
    suspend fun getEpisodeStreams(@Url url: String): TorrentioResponse
}
