package io.asanre.app.fixtures

import io.asanre.app.data.model.CharacterLocation
import io.asanre.app.data.model.CharacterResult
import io.asanre.app.data.model.CharactersResponse
import io.asanre.app.data.model.RequestInfo
import io.asanre.app.domain.entities.CharacterDetails
import io.asanre.app.domain.entities.Episode
import io.asanre.app.domain.entities.LocationExtended
import io.asanre.app.domain.entities.Status

val location = CharacterLocation("Moon", "location/1")
val origin = CharacterLocation("Earth", "location/2")
val dummyCharacterResult = CharacterResult(
    id = 1,
    name = "Morty",
    gender = "NoGen",
    image = "image",
    species = "alien",
    status = Status.Alive,
    type = "type",
    url = "url",
    episodes = listOf("episode/1"),
    location = location,
    origin = origin
)

val dummyCharacter = dummyCharacterResult.toEntity()

val dummyCharactersResponse = CharactersResponse(
    listOf(dummyCharacterResult),
    RequestInfo("next/2")
)

val dummyCharacterList = dummyCharactersResponse.toEntity()

val dummyOrigin = LocationExtended(2, "Earth", "test")
val dummyLocation = LocationExtended(1, "Moon", "test")
val dummyEpisode = Episode(1, "test1", "", "")

val dummyCharacterDetails = CharacterDetails(
    dummyCharacter,
    dummyEpisode,
    dummyOrigin,
    dummyLocation
)