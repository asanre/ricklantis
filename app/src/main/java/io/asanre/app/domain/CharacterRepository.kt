package io.asanre.app.domain

import io.asanre.app.domain.entities.CharacterEntity
import io.asanre.app.domain.entities.CharacterList

interface CharacterRepository {
    suspend fun getCharacters(currentAmount: Int): Result<CharacterList>
    suspend fun getCharacterById(id: Int): Result<CharacterEntity>
}