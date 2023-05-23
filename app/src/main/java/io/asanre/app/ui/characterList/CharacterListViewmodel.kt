package io.asanre.app.ui.characterList

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import io.asanre.app.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class CharacterListViewmodel(
    private val repository: CharacterRepository,
    coroutineContext: CoroutineContext,
    private val recompositionClock: RecompositionClock,
) : ViewModel() {
    private val scope = CoroutineScope(viewModelScope.coroutineContext + coroutineContext)
    private val events = MutableSharedFlow<CharactersEvent>(extraBufferCapacity = 20)

    val state: StateFlow<CharacterListState> by lazy(LazyThreadSafetyMode.NONE) {
        scope.launchMolecule(recompositionClock) {
            var state by remember { mutableStateOf(CharacterListState.INITIAL) }

            LaunchedEffect(Unit) {
                events.collect { event ->
                    when (event) {
                        CharactersEvent.GetCharacters -> {
                            state.count?.also { count ->
                                state = state.showLoading()
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

    fun emit(event: CharactersEvent) {
        if (!events.tryEmit(event)) {
            error("Event buffer overflow.")
        }
    }
}