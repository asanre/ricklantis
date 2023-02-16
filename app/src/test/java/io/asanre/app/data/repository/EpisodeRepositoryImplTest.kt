package io.asanre.app.data.repository

import io.asanre.app.data.service.EpisodeApiService
import io.asanre.app.fixtures.dummyEpisode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class EpisodeRepositoryImplTest {

    private val apiService = mockk<EpisodeApiService>()

    private val sut = EpisodeRepositoryImpl(apiService)


    @Test
    fun `when requesting an already requested episode then return cache value`() = runTest {
        val episodeID = 1
        coEvery { apiService.getEpisode(episodeID) } returns Result.success(dummyEpisode)

        sut.getEpisode(episodeID)
        sut.getEpisode(episodeID)
        coVerify(atLeast = 1) { apiService.getEpisode(episodeID) }
    }

    @Test
    fun `given new episode request when error then return failure`() = runTest {
        val episodeID = 1
        coEvery { apiService.getEpisode(episodeID) } returns Result.failure(Error("error"))

        assertTrue(sut.getEpisode(episodeID).isFailure)
    }
}