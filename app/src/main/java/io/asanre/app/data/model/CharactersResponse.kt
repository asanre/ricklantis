package io.asanre.app.data.model

import com.google.gson.annotations.SerializedName
import io.asanre.app.domain.entities.CharacterEntity
import io.asanre.app.domain.entities.CharacterList
import io.asanre.app.domain.entities.Location
import io.asanre.app.domain.entities.Status

data class CharactersResponse(
    val results: List<CharacterResult> = emptyList(),
    val info: RequestInfo
) {
    fun toEntity() = CharacterList(
        characters = results.map { it.toEntity() },
        allCharacterLoaded = info.next == null
    )
}

data class RequestInfo(
    val next: String?
)

data class CharacterResult(
    val id: Int,
    val name: String,
    val gender: String,
    val image: String,
    val species: String,
    val status: Status,
    val type: String,
    val url: String,
    @SerializedName("episode")
    val episodes: List<String>,
    val location: CharacterLocation,
    val origin: CharacterLocation,
) {
    fun toEntity() =
        CharacterEntity(
            id = id,
            name = name,
            imageUrl = image,
            status = status,
            species = species,
            type = type.takeIf { it.isNotEmpty() },
            gender = gender,
            origin = origin.toLocation(),
            lastLocation = location.toLocation(),
            episodes = episodes
        )
}

data class CharacterLocation(
    val name: String,
    val url: String
) {
    fun toLocation(): Location {
        return Location(
            id = url.extractId(),
            name = name,
        )
    }
}