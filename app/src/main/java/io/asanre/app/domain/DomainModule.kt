package io.asanre.app.domain

import io.asanre.app.domain.usecase.GetCharacterDetailsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetCharacterDetailsUseCase)
}