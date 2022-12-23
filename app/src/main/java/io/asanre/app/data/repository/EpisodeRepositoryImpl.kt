package io.asanre.app.data.repository

import io.asanre.app.data.service.EpisodeApiService
import io.asanre.app.domain.repository.EpisodeRepository
import io.asanre.app.domain.entities.Episode

class EpisodeRepositoryImpl(
    private val apiService: EpisodeApiService
) : EpisodeRepository {
    private var cache: Map<Int, Episode> = mapOf()

    override suspend fun getEpisode(id: Int): Result<Episode> =
        cache[id]?.let { Result.success(it) }
            ?: apiService.getEpisode(id)
                .onSuccess { cache += id to it }
}