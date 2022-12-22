package io.asanre.app.data.repository

import io.asanre.app.data.service.CharacterApiService
import io.asanre.app.domain.CharacterRepository
import io.asanre.app.domain.entities.CharacterEntity

class CharacterRepositoryImpl(
    private val api: CharacterApiService
) : CharacterRepository {

    override suspend fun getCharacters(currentAmount: Int): Result<List<CharacterEntity>> {
        return api.getAllCharacters(getNextPage(currentAmount))
            .map { response -> response.results.map { it.toEntity() } }
    }

    override suspend fun getCharacterById(id: Int): Result<CharacterEntity> {
        return api.getCharacterById(id).map { it.toEntity() }
    }

    companion object {
        private const val API_PAGE_SIZE = 20
        private fun getNextPage(totalAmount: Int): Int {
            return (totalAmount / API_PAGE_SIZE) + 1
        }
    }
}