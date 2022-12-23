package io.asanre.app.domain.repository

import io.asanre.app.domain.entities.LocationExtended

interface LocationRepository {
    suspend fun getLocation(id: Int?): Result<LocationExtended>
}