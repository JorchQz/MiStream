package com.mistream.app.data.model

import com.google.gson.annotations.SerializedName

data class TmdbMovieResult(
    @SerializedName("results") val results: List<TmdbMovie> = emptyList(),
    @SerializedName("total_pages") val totalPages: Int = 0
)

data class TmdbMovie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("first_air_date") val firstAirDate: String = "",
    @SerializedName("media_type") val mediaType: String = "",
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("imdb_id") val imdbId: String? = null
) {
    val displayTitle: String get() = title.ifEmpty { name }
    val displayDate: String get() = releaseDate.ifEmpty { firstAirDate }
    val posterUrl: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    val backdropUrl: String? get() = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" }
}

data class TmdbMovieDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("imdb_id") val imdbId: String? = null,
    @SerializedName("title") val title: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("first_air_date") val firstAirDate: String = "",
    @SerializedName("runtime") val runtime: Int? = null,
    @SerializedName("genres") val genres: List<TmdbGenre> = emptyList(),
    @SerializedName("seasons") val seasons: List<TmdbSeason> = emptyList()
) {
    val displayTitle: String get() = title.ifEmpty { name }
    val displayDate: String get() = releaseDate.ifEmpty { firstAirDate }
    val posterUrl: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    val backdropUrl: String? get() = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" }
}

data class TmdbGenre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class TmdbSeason(
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("episode_count") val episodeCount: Int,
    @SerializedName("name") val name: String = ""
)

data class TmdbSeasonDetail(
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("episodes") val episodes: List<TmdbEpisode> = emptyList()
)

data class TmdbEpisode(
    @SerializedName("episode_number") val episodeNumber: Int,
    @SerializedName("name") val name: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("still_path") val stillPath: String? = null
)

data class TorrentioResponse(
    @SerializedName("streams") val streams: List<StreamItem> = emptyList()
)

data class StreamItem(
    @SerializedName("name") val name: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("url") val url: String? = null,
    @SerializedName("infoHash") val infoHash: String? = null,
    @SerializedName("fileIdx") val fileIdx: Int? = null,
    @SerializedName("behaviorHints") val behaviorHints: BehaviorHints? = null
) {
    val quality: String get() = when {
        name.contains("4K", ignoreCase = true) || name.contains("2160", ignoreCase = true) -> "4K"
        name.contains("1080", ignoreCase = true) -> "1080p"
        name.contains("720", ignoreCase = true) -> "720p"
        else -> "SD"
    }
    val isLatino: Boolean get() {
        val check = title.lowercase() + name.lowercase()
        return check.contains("latino") || check.contains("lat]") || check.contains("[lat")
    }
    val isCastellano: Boolean get() {
        val check = title.lowercase() + name.lowercase()
        return check.contains("castellano") || check.contains("spanish") || check.contains("[esp")
    }
    val isRealDebrid: Boolean get() = url?.contains("real-debrid") == true ||
            name.contains("[RD]", ignoreCase = true)
    val sortPriority: Int get() {
        val q = when (quality) { "4K" -> 4; "1080p" -> 3; "720p" -> 2; else -> 1 }
        val l = when { isLatino -> 20; isCastellano -> 10; else -> 0 }
        return l + q
    }
}

data class BehaviorHints(
    @SerializedName("filename") val filename: String? = null,
    @SerializedName("videoSize") val videoSize: Long? = null
)

data class RealDebridUser(
    @SerializedName("username") val username: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("premium") val premium: Int = 0,
    @SerializedName("expiration") val expiration: String = ""
) { val isPremium: Boolean get() = premium > 0 }

data class RealDebridAddMagnet(
    @SerializedName("id") val id: String = "",
    @SerializedName("uri") val uri: String = ""
)

data class RealDebridTorrentInfo(
    @SerializedName("id") val id: String = "",
    @SerializedName("filename") val filename: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("files") val files: List<RealDebridFile> = emptyList(),
    @SerializedName("links") val links: List<String> = emptyList()
)

data class RealDebridFile(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("path") val path: String = "",
    @SerializedName("bytes") val bytes: Long = 0,
    @SerializedName("selected") val selected: Int = 0
)

data class RealDebridUnrestrict(
    @SerializedName("id") val id: String = "",
    @SerializedName("filename") val filename: String = "",
    @SerializedName("link") val link: String = "",
    @SerializedName("download") val download: String = "",
    @SerializedName("streamable") val streamable: Int = 0,
    @SerializedName("filesize") val filesize: Long = 0,
    @SerializedName("mimeType") val mimeType: String = ""
)