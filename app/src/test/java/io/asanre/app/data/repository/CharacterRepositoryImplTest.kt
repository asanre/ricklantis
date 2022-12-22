package io.asanre.app.data.repository

import io.asanre.app.data.model.CharacterLocation
import io.asanre.app.data.model.CharacterResult
import io.asanre.app.data.model.CharactersResponse
import io.asanre.app.data.service.CharacterApiService
import io.asanre.app.domain.entities.Status
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CharacterRepositoryImplTest {
    private val apiService = mockk<CharacterApiService>()
    private val sut = CharacterRepositoryImpl(apiService)


    private val location = CharacterLocation("Moon", "location/1")
    private val characterResult = CharacterResult(
        1,
        "Morty",
        "NoGen",
        "image",
        "alien",
        Status.Alive,
        "type",
        "url",
        listOf("episode/1"),
        location,
        location
    )
    private val successCharactersResponse = CharactersResponse(listOf(characterResult))

    @ParameterizedTest
    @CsvSource("0, 1", "21, 2")
    fun `given totalAmount then request nextPage`(totalAmount: Int, nextPage: Int) = runTest {
        coEvery { apiService.getAllCharacters(any()) } returns Result.failure(Error("Error"))

        sut.getCharacters(totalAmount)
        coVerify { apiService.getAllCharacters(nextPage) }
    }

    @Test
    fun `given a success getCharacters request then return a valid list`() = runTest {
        coEvery { apiService.getAllCharacters(any()) } returns Result.success(successCharactersResponse)

        val result = sut.getCharacters(0)
        assertEquals(successCharactersResponse.results.map { it.toEntity() }, result.getOrNull())
    }

    @Test
    fun `given a failure getCharacters request then return an error`() = runTest {
        coEvery { apiService.getAllCharacters(any()) } returns Result.failure(Error("error"))

        val result = sut.getCharacters(0)
        assert(result.isFailure)
    }

    @Test
    fun `given a success getCharacterById request then return a valid character`() = runTest {
        coEvery { apiService.getCharacterById(any()) } returns Result.success(characterResult)

        val result = sut.getCharacterById(1)
        assertEquals(characterResult.toEntity(), result.getOrNull())
    }

    @Test
    fun `given a failure getCharacterById then return an Error`() = runTest {
        coEvery { apiService.getCharacterById(any()) } returns Result.failure(Error("error"))

        val result = sut.getCharacterById(1)
        assert(result.isFailure)
    }
}