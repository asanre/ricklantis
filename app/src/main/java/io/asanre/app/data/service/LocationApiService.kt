package io.asanre.app.data.service

import io.asanre.app.domain.entities.LocationExtended
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.resources.*

class LocationApiService(private val client: HttpClient) {
    suspend fun getLocation(id: Int): Result<LocationExtended> = runCatching {
        client.get(Locations.Id(id)).body()
    }

    @Resource("/location")
    private class Locations() {
        @Resource("{id}")
        class Id(val id: Int, val parent: Locations = Locations())
    }
}