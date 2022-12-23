package io.asanre.app.data.repository

import io.asanre.app.data.service.LocationApiService
import io.asanre.app.domain.repository.LocationRepository
import io.asanre.app.domain.entities.LocationExtended

class LocationRepositoryImpl(
    private val apiService: LocationApiService
) : LocationRepository {
    private var cache: Map<Int, LocationExtended> = mapOf()

    override suspend fun getLocation(id: Int?): Result<LocationExtended> =
        id?.let { id ->
            cache[id]?.let { Result.success(it) }
                ?: apiService.getLocation(id)
                    .onSuccess { cache += id to it }
        } ?: Result.success(LocationExtended(null, "unknown", null))
}