package io.asanre.app.domain.entities

import com.google.gson.annotations.SerializedName

data class CharacterList(
    val characters: List<Character>,
    val allCharacterLoaded: Boolean
)

data class Character(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: Status,
    val species: String,
    val type: String?,
    val gender: String,
    val origin: Location,
    val lastLocation: Location,
    val episodes: List<String>,
)

data class CharacterDetails(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: Status,
    val species: String,
    val type: String?,
    val gender: String,
    val origin: LocationExtended?,
    val lastLocation: LocationExtended?,
    val firstEpisode: Episode?,
) {
    constructor(
        character: Character,
        firstEpisode: Episode?,
        origin: LocationExtended?,
        lastLocation: LocationExtended?,
    ) : this(
        id = character.id,
        name = character.name,
        imageUrl = character.imageUrl,
        status = character.status,
        species = character.species,
        type = character.type,
        gender = character.gender,
        origin = origin,
        lastLocation = lastLocation,
        firstEpisode = firstEpisode
    )
}

enum class Status {
    @SerializedName("Alive")
    Alive,

    @SerializedName("Dead")
    Dead,

    @SerializedName("unknown")
    Unknown,
}