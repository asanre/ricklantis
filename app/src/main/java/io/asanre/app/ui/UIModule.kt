package io.asanre.app.ui

import io.asanre.app.ui.characterList.CharacterListViewmodel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        CharacterListViewmodel(get())
    }
}