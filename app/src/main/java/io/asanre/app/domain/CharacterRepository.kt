package io.asanre.app.domain

import io.asanre.app.domain.entities.CharacterEntity

interface CharacterRepository {
    suspend fun getCharacters(currentAmount: Int): Result<List<CharacterEntity>>
    suspend fun getCharacterById(id: Int): Result<CharacterEntity>
}