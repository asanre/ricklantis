package io.asanre.app.ui.characterDetail

import androidx.lifecycle.ViewModel
import io.asanre.app.core.ui.launch
import io.asanre.app.domain.usecase.GetCharacterDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CharacterDetailViewmodel(
    private val getCharacterDetails: GetCharacterDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterDetailState.INITIAL)
    val state: StateFlow<CharacterDetailState> = _state

    fun getDetail(id: Int) = launch {
        _state.update { CharacterDetailState.INITIAL }
        getCharacterDetails(id).onSuccess { detail ->
            _state.update { it.displayDetail(detail) }
        }.onFailure {
            _state.update { it.onError() }
        }
    }

    fun onErrorShown() = _state.update { it.dismissError() }
}