package com.mistream.app.data.api

import com.google.gson.annotations.SerializedName
import com.mistream.app.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class ExternalIds(
    @SerializedName("imdb_id") val imdbId: String? = null
)

interface TmdbApiService {
    @GET("trending/all/week")
    suspend fun getTrending(@Query("language") language: String = "es-MX"): TmdbMovieResult

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "es-MX",
        @Query("page") page: Int = 1
    ): TmdbMovieResult

    @GET("tv/popular")
    suspend fun getPopularSeries(
        @Query("language") language: String = "es-MX",
        @Query("page") page: Int = 1
    ): TmdbMovieResult

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("language") language: String = "es-MX"
    ): TmdbMovieResult

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("language") language: String = "es-MX"
    ): TmdbMovieDetail

    @GET("tv/{id}")
    suspend fun getTvDetail(
        @Path("id") id: Int,
        @Query("language") language: String = "es-MX"
    ): TmdbMovieDetail

    @GET("movie/{id}/external_ids")
    suspend fun getMovieExternalIds(@Path("id") id: Int): ExternalIds

    @GET("tv/{id}/external_ids")
    suspend fun getTvExternalIds(@Path("id") id: Int): ExternalIds

    @GET("tv/{id}/season/{season_number}")
    suspend fun getSeasonDetail(
        @Path("id") id: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("language") language: String = "es-MX"
    ): TmdbSeasonDetail
}
