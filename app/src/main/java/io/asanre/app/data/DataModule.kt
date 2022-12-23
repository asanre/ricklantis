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
import retrofit2.Retrofit
import retrofit2.create

val dataModule = module {
    single { get<Retrofit>().create<CharacterApiService>() }
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }

    single { get<Retrofit>().create<EpisodeApiService>() }
    single<EpisodeRepository> { EpisodeRepositoryImpl(get()) }

    single { get<Retrofit>().create<LocationApiService>() }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
}