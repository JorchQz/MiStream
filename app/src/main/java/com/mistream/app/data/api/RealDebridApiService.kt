package com.mistream.app.data.api

import com.mistream.app.data.model.*
import retrofit2.http.*

interface RealDebridApiService {
    @GET("user")
    suspend fun getUser(): RealDebridUser

    @FormUrlEncoded
    @POST("torrents/addMagnet")
    suspend fun addMagnet(@Field("magnet") magnet: String): RealDebridAddMagnet

    @GET("torrents/info/{id}")
    suspend fun getTorrentInfo(@Path("id") id: String): RealDebridTorrentInfo

    @FormUrlEncoded
    @POST("torrents/selectFiles/{id}")
    suspend fun selectFiles(@Path("id") id: String, @Field("files") files: String)

    @FormUrlEncoded
    @POST("unrestrict/link")
    suspend fun unrestrictLink(@Field("link") link: String): RealDebridUnrestrict

    @DELETE("torrents/delete/{id}")
    suspend fun deleteTorrent(@Path("id") id: String)
}
