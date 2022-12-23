package io.asanre.app.data.service

import io.asanre.app.domain.entities.Location
import io.asanre.app.domain.entities.LocationExtended
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationApiService {
    @GET("/api/location/{id}")
    suspend fun getLocation(@Path("id") id: Int): Result<LocationExtended>
}