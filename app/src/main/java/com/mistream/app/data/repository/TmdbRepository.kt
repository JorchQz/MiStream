package com.mistream.app.data.repository

import com.mistream.app.data.api.TmdbApiService
import com.mistream.app.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbRepository @Inject constructor(
    private val api: TmdbApiService
) {
    suspend fun getTrending(): List<TmdbMovie> = api.getTrending().results

    suspend fun getPopularMovies(): List<TmdbMovie> = api.getPopularMovies().results

    suspend fun getPopularSeries(): List<TmdbMovie> = api.getPopularSeries().results

    suspend fun searchMulti(query: String): List<TmdbMovie> = api.searchMulti(query).results

    suspend fun getMovieDetail(id: Int): TmdbMovieDetail = api.getMovieDetail(id)

    suspend fun getTvDetail(id: Int): TmdbMovieDetail = api.getTvDetail(id)

    suspend fun getImdbId(id: Int, isMovie: Boolean): String? =
        if (isMovie) {
            val detail = api.getMovieDetail(id)
            detail.imdbId ?: api.getMovieExternalIds(id).imdbId
        } else {
            api.getTvExternalIds(id).imdbId
        }

    suspend fun getSeasonDetail(id: Int, seasonNumber: Int): TmdbSeasonDetail =
        api.getSeasonDetail(id, seasonNumber)
}
