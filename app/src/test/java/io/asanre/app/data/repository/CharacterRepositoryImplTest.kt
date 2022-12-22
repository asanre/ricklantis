package io.asanre.app.data.repository

import io.asanre.app.data.model.RequestInfo
import io.asanre.app.data.service.CharacterApiService
import io.asanre.app.fixtures.dummyCharacterResult
import io.asanre.app.fixtures.dummyCharactersResponse
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


    @ParameterizedTest
    @CsvSource("0, 1", "21, 2")
    fun `given totalAmount then request nextPage`(totalAmount: Int, nextPage: Int) = runTest {
        coEvery { apiService.getAllCharacters(any()) } returns Result.failure(Error("Error"))

        sut.getCharacters(totalAmount)
        coVerify { apiService.getAllCharacters(nextPage) }
    }

    @Test
    fun `given a success getCharacters request then return a valid list`() = runTest {
        coEvery { apiService.getAllCharacters(any()) } returns Result.success(
            dummyCharactersResponse
        )

        val result = sut.getCharacters(0).getOrThrow()
        assertEquals(dummyCharactersResponse.toEntity().characters, result.characters)
        assertFalse(result.allCharacterLoaded)
    }

    @Test
    fun `given a success getCharacters request when there are not more characters then notify allCharacterLoaded`() =
        runTest {
            coEvery { apiService.getAllCharacters(any()) } returns Result.success(
                dummyCharactersResponse.copy(info = RequestInfo(null))
            )

            val result = sut.getCharacters(0).getOrThrow()
            assertEquals(dummyCharactersResponse.toEntity().characters, result.characters)
            assertTrue(result.allCharacterLoaded)
        }

    @Test
    fun `given a failure getCharacters request then return an error`() = runTest {
        coEvery { apiService.getAllCharacters(any()) } returns Result.failure(Error("error"))

        val result = sut.getCharacters(0)
        assert(result.isFailure)
    }

    @Test
    fun `given a success getCharacterById request then return a valid character`() = runTest {
        coEvery { apiService.getCharacterById(any()) } returns Result.success(dummyCharacterResult)

        val result = sut.getCharacterById(1)
        assertEquals(dummyCharacterResult.toEntity(), result.getOrNull())
    }

    @Test
    fun `given a failure getCharacterById then return an Error`() = runTest {
        coEvery { apiService.getCharacterById(any()) } returns Result.failure(Error("error"))

        val result = sut.getCharacterById(1)
        assert(result.isFailure)
    }
}