package com.mistream.app.data.repository

import com.mistream.app.data.api.RealDebridApiService
import com.mistream.app.data.model.RealDebridUser
import com.mistream.app.data.model.StreamItem
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealDebridRepository @Inject constructor(
    private val api: RealDebridApiService
) {
    suspend fun getUser(): RealDebridUser = api.getUser()

    suspend fun resolveStream(stream: StreamItem): String? = when {
        stream.url != null && stream.isRealDebrid ->
            runCatching { api.unrestrictLink(stream.url).download }.getOrNull()
        stream.url != null -> stream.url
        stream.infoHash != null -> resolveInfoHash(stream.infoHash, stream.fileIdx)
        else -> null
    }

    private suspend fun resolveInfoHash(infoHash: String, fileIdx: Int?): String? {
        val magnet = "magnet:?xt=urn:btih:$infoHash"
        val torrentId = runCatching { api.addMagnet(magnet).id }.getOrNull() ?: return null

        val filesParam = fileIdx?.toString() ?: "all"
        runCatching { api.selectFiles(torrentId, filesParam) }

        repeat(12) { attempt ->
            delay(3000L + attempt * 1000L)
            val info = runCatching { api.getTorrentInfo(torrentId) }.getOrNull() ?: return@repeat
            if (info.status == "downloaded" && info.links.isNotEmpty()) {
                val link = if (fileIdx != null && fileIdx < info.links.size) info.links[fileIdx]
                           else info.links.first()
                val download = runCatching { api.unrestrictLink(link).download }.getOrNull()
                runCatching { api.deleteTorrent(torrentId) }
                return download
            }
        }
        runCatching { api.deleteTorrent(torrentId) }
        return null
    }
}
