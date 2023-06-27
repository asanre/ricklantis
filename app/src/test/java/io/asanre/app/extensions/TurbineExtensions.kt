package io.asanre.app.extensions

import androidx.compose.runtime.Composable
import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import kotlin.time.Duration


/**
 * creates a moleculeFlow with [RecompositionClock.Immediate] recomposition clock
 * and the turbine validate function
 */
suspend fun <T> (@Composable () -> T).test(
    timeout: Duration? = null,
    name: String? = null,
    validate: suspend ReceiveTurbine<T>.() -> Unit
) = moleculeFlow(RecompositionClock.Immediate, this).test(timeout, name, validate)