package io.asanre.app.ui.characterList

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import io.asanre.app.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

class CharacterListViewmodel(
    private val repository: CharacterRepository
) : ViewModel() {
    private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    private val events = MutableSharedFlow<CharactersEvent>(extraBufferCapacity = 20)

    val state: StateFlow<CharacterListState> by lazy(LazyThreadSafetyMode.NONE) {
        scope.launchMolecule(RecompositionClock.ContextClock) {
            var state by remember { mutableStateOf(CharacterListState.INITIAL) }

            LaunchedEffect(Unit) {
                events.collect { event ->
                    when (event) {
                        CharactersEvent.GetCharacters -> {
                            state.count?.also { count ->
                                repository.getCharacters(count)
                                    .onSuccess { state = state.addCharacters(it) }
                                    .onFailure { state = state.showError() }
                            }
                        }

                        CharactersEvent.ErrorShown -> state = state.dismissError()
                    }
                }
            }
            state
        }
    }

    fun onEvent(event: CharactersEvent) {
        if (!events.tryEmit(event)) {
            error("Event buffer overflow.")
        }
    }
}