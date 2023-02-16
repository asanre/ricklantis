package io.asanre.app.domain.usecase

import io.asanre.app.domain.entities.Character
import io.asanre.app.domain.entities.Episode
import io.asanre.app.domain.entities.LocationExtended
import io.asanre.app.domain.repository.CharacterRepository
import io.asanre.app.domain.repository.EpisodeRepository
import io.asanre.app.domain.repository.LocationRepository
import io.asanre.app.fixtures.dummyCharacter
import io.asanre.app.fixtures.dummyEpisode
import io.asanre.app.fixtures.dummyLocation
import io.asanre.app.fixtures.dummyOrigin
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GetCharacterDetailsUseCaseTest {

    private val characterRepo = mockk<CharacterRepository>()
    private val locationRepo = mockk<LocationRepository>()
    private val episodeRepo = mockk<EpisodeRepository>()

    private val sut = GetCharacterDetailsUseCase(characterRepo, locationRepo, episodeRepo)

    @Test
    fun `given get character when error then avoid calling other repo`() = runTest {
        when_repo_request_should_return(
            characterResponse = Result.failure(Error("Error")),
        )
        sut(1)
        coVerify(exactly = 0) { locationRepo.getLocation(any()) }
        coVerify(exactly = 0) { episodeRepo.getEpisode(any()) }
    }

    @Test
    fun `given get character when error then return result failure`() = runTest {
        when_repo_request_should_return(
            characterResponse = Result.failure(Error("Error")),
        )

        assertTrue(sut(1).isFailure)
    }

    @Test
    fun `given location requests errors then locations should be null`() = runTest {
        when_repo_request_should_return(
            characterResponse = Result.success(dummyCharacter),
            locationResponse = Result.failure(Error("Error")),
            originResponse = Result.failure(Error("Error"))
        )
        val result = sut(1).getOrThrow()
        assertNull(result.origin)
        assertNull(result.lastLocation)
    }

    @Test
    fun `given location origin success then should not be null`() = runTest {
        when_repo_request_should_return(
            characterResponse = Result.success(dummyCharacter),
            locationResponse = Result.failure(Error("Error")),
            originResponse = Result.success(dummyOrigin)
        )

        val result = sut(1).getOrThrow()
        assertEquals(dummyOrigin, result.origin)
        assertNull(result.lastLocation)
    }

    @Test
    fun `given last location success then should not be null`() = runTest {
        when_repo_request_should_return(
            characterResponse = Result.success(dummyCharacter),
            locationResponse = Result.success(dummyLocation),
            originResponse = Result.failure(Error("Error"))
        )

        val result = sut(1).getOrThrow()
        assertEquals(dummyLocation, result.lastLocation)
        assertNull(result.origin)
    }

    @Test
    fun `given episode request error then episode should be null`() = runTest {
        when_repo_request_should_return(
            characterResponse = Result.success(dummyCharacter),
            episodeResponse = Result.failure(Error("Error")),
        )

        assertNull(sut(1).getOrThrow().firstEpisode)
    }

    @Test
    fun `given episode request success then episode should not be null`() = runTest {
        when_repo_request_should_return(
            characterResponse = Result.success(dummyCharacter),
            episodeResponse = Result.success(dummyEpisode),
        )
        assertNotNull(sut(1).getOrThrow().firstEpisode)
    }

    @Test
    fun `given get character when success then return the character detail`() = runTest {
        when_repo_request_should_return(
            characterResponse = Result.success(dummyCharacter)
        )

        val result = sut(1).getOrNull()
        assertEquals(dummyCharacter.id, result?.id)
    }

    private fun when_repo_request_should_return(
        characterResponse: Result<Character>,
        episodeResponse: Result<Episode> = Result.failure(Error("Error")),
        locationResponse: Result<LocationExtended> = Result.failure(Error("Error")),
        originResponse: Result<LocationExtended> = Result.failure(Error("Error"))
    ) {
        coEvery { characterRepo.getCharacterById(any()) } returns characterResponse
        coEvery { episodeRepo.getEpisode(any()) } returns episodeResponse
        coEvery { locationRepo.getLocation(1) } returns locationResponse
        coEvery { locationRepo.getLocation(2) } returns originResponse
    }
}