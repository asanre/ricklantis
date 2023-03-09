package io.asanre.app.data.service

import io.asanre.app.core.data.getBody
import io.asanre.app.domain.entities.Episode
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.resources.*

class EpisodeApiService(private val client: HttpClient) {

    suspend fun getEpisode(id: Int): Result<Episode> = runCatching {
        client.getBody(Episodes.Id(id))
    }

    @Resource("/episode")
    private class Episodes() {
        @Resource("{id}")
        class Id(val id: Int, val parent: Episodes = Episodes())
    }
}