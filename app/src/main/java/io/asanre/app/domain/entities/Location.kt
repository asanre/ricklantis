package io.asanre.app.domain.entities

data class Location(val id: Int?, val name: String)

data class LocationExtended(val id: Int?, val name: String, val dimension: String?)