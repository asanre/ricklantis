package io.asanre.app.domain.usecase

import io.asanre.app.domain.entities.CharacterDetails
import io.asanre.app.domain.entities.Character
import io.asanre.app.domain.repository.CharacterRepository
import io.asanre.app.domain.repository.EpisodeRepository
import io.asanre.app.domain.repository.LocationRepository
import io.asanre.app.fixtures.dummyCharacter
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GetCharacterDetailsUseCaseTest {

    private val characterRepo = mockk<CharacterRepository>()
    private val locationRepo = mockk<LocationRepository>()
    private val episodeRepo = mockk<EpisodeRepository>()

    private val sut = GetCharacterDetailsUseCase(characterRepo, locationRepo, episodeRepo)

    @Test
    fun `given get character when error then return result failure`() = runTest {
        val expected = Result.failure<Character>(Error("Error"))
        coEvery { characterRepo.getCharacterById(any()) } returns expected

        val result = sut(1)
        assertEquals(expected, result)
        coVerify(exactly = 0) { locationRepo.getLocation(any()) }
        coVerify(exactly = 0) { episodeRepo.getEpisode(any()) }
    }

    @Test
    fun `given get character when success then return the character detail`() = runTest {
        coEvery { characterRepo.getCharacterById(any()) } returns Result.success(dummyCharacter)
        coEvery { locationRepo.getLocation(any()) } returns Result.failure(Error("Error"))
        coEvery { episodeRepo.getEpisode(any()) } returns Result.failure(Error("Error"))

        val expected = CharacterDetails(dummyCharacter, null, null, null)
        val result = sut(1).getOrNull()
        assertEquals(expected, result)
        coVerify(exactly = 2) { locationRepo.getLocation(any()) }
        coVerify(exactly = 1) { episodeRepo.getEpisode(any()) }
    }
}