package io.asanre.app.data

import io.asanre.app.data.repository.CharacterRepositoryImpl
import io.asanre.app.data.repository.EpisodeRepositoryImpl
import io.asanre.app.data.repository.LocationRepositoryImpl
import io.asanre.app.data.service.CharacterApiService
import io.asanre.app.data.service.EpisodeApiService
import io.asanre.app.data.service.LocationApiService
import io.asanre.app.domain.repository.CharacterRepository
import io.asanre.app.domain.repository.EpisodeRepository
import io.asanre.app.domain.repository.LocationRepository
import org.koin.dsl.module

val dataModule = module {
    single { CharacterApiService(get()) }
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }

    single { EpisodeApiService(get()) }
    single<EpisodeRepository> { EpisodeRepositoryImpl(get()) }

    single { LocationApiService(get()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
}