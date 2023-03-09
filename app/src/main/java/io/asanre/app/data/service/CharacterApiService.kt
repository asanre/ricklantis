package io.asanre.app.data.service

import io.asanre.app.data.model.CharacterResult
import io.asanre.app.data.model.CharactersResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class CharacterApiService(
    private val client: HttpClient
) {
    suspend fun getCharacterById(id: Int): Result<CharacterResult> {
        return runCatching {
            val response = client.get("api/character/$id")
            response.body()
        }
    }

    suspend fun getAllCharacters(page: Int): Result<CharactersResponse> {
        return runCatching {
            val response = client.get("api/character?page=$page")
            response.body()
        }
    }
}