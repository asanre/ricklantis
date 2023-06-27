package io.asanre.app.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

/**
 * Creates a lazy stateFlow using [launchMolecule] and [RecompositionClock.ContextClock]
 */
inline fun <T> ViewModel.moleculeStateFlow(
    clockContext: CoroutineContext = AndroidUiDispatcher.Main,
    clock: RecompositionClock = RecompositionClock.ContextClock,
    safetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
    crossinline body: @Composable () -> T
): Lazy<StateFlow<T>> = lazy(safetyMode) {
    val scope = CoroutineScope(viewModelScope.coroutineContext + clockContext)
    scope.launchMolecule(clock) { body() }
}