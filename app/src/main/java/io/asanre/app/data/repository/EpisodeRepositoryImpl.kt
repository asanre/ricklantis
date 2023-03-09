package io.asanre.app.data.repository

import io.asanre.app.data.service.EpisodeApiService
import io.asanre.app.domain.entities.Episode
import io.asanre.app.domain.repository.EpisodeRepository

class EpisodeRepositoryImpl(
    private val apiService: EpisodeApiService
) : EpisodeRepository {

    override suspend fun getEpisode(id: Int): Result<Episode> =
        apiService.getEpisode(id)
}