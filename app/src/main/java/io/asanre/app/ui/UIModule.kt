package io.asanre.app.ui

import androidx.compose.ui.platform.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import io.asanre.app.ui.characterDetail.CharacterDetailViewmodel
import io.asanre.app.ui.characterList.CharacterListViewmodel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    factory { AndroidUiDispatcher.Main }
    viewModel { CharacterDetailViewmodel(get()) }
    viewModel { CharacterListViewmodel(get(), get(), RecompositionClock.ContextClock) }
}