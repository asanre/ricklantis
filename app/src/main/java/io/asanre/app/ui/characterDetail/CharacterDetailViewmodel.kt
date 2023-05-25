package io.asanre.app.ui.characterDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.asanre.app.domain.entities.CharacterDetails
import io.asanre.app.domain.usecase.GetCharacterDetailsUseCase
import io.asanre.app.ui.characterDetail.CharacterDetailScreen.Event
import io.asanre.app.ui.characterDetail.CharacterDetailScreen.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class ViewModel : InstanceKeeper.Instance, CoroutineScope {
    override val coroutineContext: CoroutineContext = AndroidUiDispatcher.Main + SupervisorJob()
    override fun onDestroy() = coroutineContext.cancel()
}

/**
 * Creates a lazy stateFlow using [launchMolecule] and [RecompositionClock.ContextClock]
 */
inline fun <T> CoroutineScope.moleculeState(
    clock: RecompositionClock = RecompositionClock.ContextClock,
    safetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
    crossinline body: @Composable () -> T
): Lazy<StateFlow<T>> =
    lazy(safetyMode) { launchMolecule(clock) { body() } }

class CharacterDetailViewmodel(private val getCharacterDetails: GetCharacterDetailsUseCase) :
    ViewModel() {

    val state: StateFlow<State> by moleculeState {
        CharacterDetailPresenter(getCharacterDetails)
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