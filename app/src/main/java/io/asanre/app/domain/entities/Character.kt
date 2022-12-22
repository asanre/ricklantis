package io.asanre.app.domain.entities

import com.google.gson.annotations.SerializedName

data class CharacterEntity(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: Status,
    val species: String,
    val type: String?,
    val gender: String,
    val origin: Location,
    val currentLocation: Location,
    val episodes: List<String>,
)

enum class Status {
    @SerializedName("Alive")
    Alive,

    @SerializedName("Dead")
    Dead,

    @SerializedName("unknown")
    Unknown,
}