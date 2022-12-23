package io.asanre.app.data.service

import io.asanre.app.domain.entities.Episode
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeApiService {
    @GET("/api/episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Result<Episode>
}