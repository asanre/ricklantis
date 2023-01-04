package io.asanre.app.domain.repository

import io.asanre.app.domain.entities.Character
import io.asanre.app.domain.entities.CharacterList

interface CharacterRepository {
    suspend fun getCharacters(currentAmount: Int): Result<CharacterList>
    suspend fun getCharacterById(id: Int): Result<Character>
}