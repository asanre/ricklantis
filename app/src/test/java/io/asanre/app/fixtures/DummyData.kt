package io.asanre.app.fixtures

import io.asanre.app.data.model.CharacterLocation
import io.asanre.app.data.model.CharacterResult
import io.asanre.app.data.model.CharactersResponse
import io.asanre.app.data.model.RequestInfo
import io.asanre.app.domain.entities.Status

val location = CharacterLocation("Moon", "location/1")
val dummyCharacterResult = CharacterResult(
    1,
    "Morty",
    "NoGen",
    "image",
    "alien",
    Status.Alive,
    "type",
    "url",
    listOf("episode/1"),
    location,
    location
)
val dummyCharactersResponse = CharactersResponse(
    listOf(dummyCharacterResult),
    RequestInfo("next/2")
)

val dummyCharacterList = dummyCharactersResponse.toEntity()