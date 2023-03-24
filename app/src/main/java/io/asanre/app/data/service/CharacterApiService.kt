package io.asanre.app.data.service

import io.asanre.app.core.data.getResult
import io.asanre.app.data.model.CharacterResult
import io.asanre.app.data.model.CharactersResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.resources.*

class CharacterApiService(private val client: HttpClient) {
    suspend fun getAllCharacters(page: Int): Result<CharactersResponse> =
        client.getResult(Characters(page))

    suspend fun getCharacterById(id: Int): Result<CharacterResult> =
        client.getResult(Characters.ById(id))

    @Resource("/character")
    private class Characters(val page: Int? = null) {

        @Resource("{id}")
        class ById(val id: Int, val parent: Characters = Characters())
    }
}