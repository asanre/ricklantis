package io.asanre.app.domain.entities

data class Location(val id: Int?, val name: String)

data class LocationExtended(
    val id: Int?,
    private val name: String,
    private val dimension: String?
) {
    val value: String
        get() = if (dimension == null) {
            name
        } else {
            "$name - $dimension"
        }
}