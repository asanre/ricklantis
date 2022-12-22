package io.asanre.app.data

import io.asanre.app.data.repository.CharacterRepositoryImpl
import io.asanre.app.data.service.CharacterApiService
import io.asanre.app.domain.CharacterRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val dataModule = module {
    single { get<Retrofit>().create<CharacterApiService>() }
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
}