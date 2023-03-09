package io.asanre.app.data.service

import io.asanre.app.core.data.getBody
import io.asanre.app.data.model.CharacterResult
import io.asanre.app.data.model.CharactersResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.resources.*

class CharacterApiService(private val client: HttpClient) {
    suspend fun getCharacterById(id: Int): Result<CharacterResult> {
        return runCatching {
            client.getBody(Characters.ById(id))
        }
    }

    suspend fun getAllCharacters(page: Int): Result<CharactersResponse> {
        return runCatching {
            client.getBody(Characters(page))
        }
    }

    @Resource("/character")
    private class Characters(val page: Int? = null) {

        @Resource("{id}")
        class ById(val id: Int, val parent: Characters = Characters())
    }
}