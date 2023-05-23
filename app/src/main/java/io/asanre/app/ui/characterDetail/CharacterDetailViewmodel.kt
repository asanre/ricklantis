package io.asanre.app.ui.characterDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import io.asanre.app.domain.entities.CharacterDetails
import io.asanre.app.domain.usecase.GetCharacterDetailsUseCase
import io.asanre.app.ui.characterDetail.CharacterDetailScreen.Event
import io.asanre.app.ui.characterDetail.CharacterDetailScreen.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CharacterDetailViewmodel(
    context: CoroutineContext,
    private val getCharacterDetails: GetCharacterDetailsUseCase,
) : ViewModel() {
    private val scope = CoroutineScope(viewModelScope.coroutineContext + context)

    val state: StateFlow<State> by lazy(LazyThreadSafetyMode.NONE) {
        scope.launchMolecule(RecompositionClock.ContextClock) {
            CharacterDetailPresenter(getCharacterDetails)
        }
    }
}

@Composable
fun CharacterDetailPresenter(getCharacterDetails: GetCharacterDetailsUseCase): State {
    val scope = rememberCoroutineScope()
    var characterDetails: CharacterDetails? by remember { mutableStateOf(null) }
    var error: Boolean by remember { mutableStateOf(false) }


    return State(
        value = characterDetails,
        error = error,
        onEvent = { event ->
            when (event) {
                is Event.GetDetail -> {
                    characterDetails = null
                    scope.launch {
                        getCharacterDetails(event.id)
                            .onSuccess { characterDetails = it }
                            .onFailure { error = true }
                    }
                }

                is Event.ErrorShown -> error = false
            }
        }
    )
}