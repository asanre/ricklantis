package io.asanre.app.ui.characterList

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import app.cash.molecule.RecompositionClock
import io.asanre.app.core.ui.extensions.moleculeStateFlow
import io.asanre.app.domain.repository.CharacterRepository
import io.asanre.app.ui.characterList.CharactersScreen.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class CharacterListViewmodel(
    private val repository: CharacterRepository,
    context: CoroutineContext,
    clock: RecompositionClock
) : ViewModel() {
    private val events = MutableSharedFlow<Event>(extraBufferCapacity = 20)

    val state: StateFlow<CharactersScreen.State> by moleculeStateFlow(context, clock) {
        var state by remember { mutableStateOf(CharactersScreen.State.INITIAL) }

        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    Event.GetCharacters -> {
                        state.count?.also { count ->
                            state = state.showLoading()
                            repository.getCharacters(count)
                                .onSuccess { state = state.addCharacters(it) }
                                .onFailure { state = state.showError() }
                        }
                    }

                    Event.ErrorShown -> state = state.dismissError()
                }
            }
        }
        state
    }

    fun emit(event: Event) = events.tryEmit(event)
}