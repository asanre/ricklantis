package io.asanre.app.domain.repository

import io.asanre.app.domain.entities.Episode


interface EpisodeRepository {
    suspend fun getEpisode(id: Int): Result<Episode>
}