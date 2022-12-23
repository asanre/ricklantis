package io.asanre.app.domain.usecase

import io.asanre.app.data.model.extractId
import io.asanre.app.domain.repository.CharacterRepository
import io.asanre.app.domain.repository.EpisodeRepository
import io.asanre.app.domain.repository.LocationRepository
import io.asanre.app.domain.entities.CharacterDetails
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetCharacterDetailsUseCase(
    private val characterRepository: CharacterRepository,
    private val locationRepository: LocationRepository,
    private val episodeRepository: EpisodeRepository,
) {
    suspend operator fun invoke(id: Int): Result<CharacterDetails> = coroutineScope {
        characterRepository.getCharacterById(id).mapCatching { character ->
            val episode = async {
                character.episodes.first().extractId()?.let {
                    episodeRepository.getEpisode(it).getOrNull()
                }
            }

            val lastLocation = async {
                locationRepository.getLocation(character.lastLocation.id).getOrNull()
            }

            val origin = async {
                locationRepository.getLocation(character.origin.id).getOrNull()
            }

            CharacterDetails(
                character = character,
                origin = origin.await(),
                firstEpisode = episode.await(),
                lastLocation = lastLocation.await(),
            )
        }
    }
}