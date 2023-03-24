package io.asanre.app.data.repository

import io.asanre.app.data.service.LocationApiService
import io.asanre.app.domain.entities.LocationExtended
import io.asanre.app.domain.repository.LocationRepository

class LocationRepositoryImpl(
    private val apiService: LocationApiService
) : LocationRepository {
    override suspend fun getLocation(id: Int?): Result<LocationExtended> =
        id?.let { id ->
            apiService.getLocation(id)
        } ?: Result.success(LocationExtended(null, "unknown", null))
}