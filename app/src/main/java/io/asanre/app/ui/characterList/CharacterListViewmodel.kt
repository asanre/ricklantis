package io.asanre.app.ui.characterList

import androidx.lifecycle.ViewModel
import io.asanre.app.core.ui.launch
import io.asanre.app.domain.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CharacterListViewmodel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterListState.INITIAL)
    val state: StateFlow<CharacterListState> = _state


    fun getCharacters() = launch {
        _state.value.count?.also { currentAmount ->
            repository.getCharacters(currentAmount)
                .onSuccess {
                    _state.update { state -> state.addCharacters(it) }
                }
                .onFailure {
                    _state.update { it.showError() }
                }
        }
    }

    fun onErrorShown() = _state.update { it.dismissError() }
}