package io.asanre.app.data.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import io.asanre.app.data.model.CharactersResponse
import io.asanre.app.data.model.CharacterResult

interface CharacterApiService {

    @GET("/api/character")
    suspend fun getAllCharacters(@Query("page") page: Int): Result<CharactersResponse>

    @GET("/api/character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Result<CharacterResult>
}