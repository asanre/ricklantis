package io.asanre.app.core.ui.components.modal

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ModalBottomSheetValue.Expanded
import androidx.compose.material.ModalBottomSheetValue.HalfExpanded
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.asanre.app.core.ui.components.modal.SheetMode.DYNAMIC
import io.asanre.app.core.ui.components.modal.SheetMode.EXPANDABLE
import io.asanre.app.core.ui.components.modal.SheetMode.FULL_HEIGHT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val ModalBottomSheetValue.isHidden
    get() = this == Hidden

val ModalBottomSheetValue.isHalfExpanded
    get() = this == HalfExpanded

val ModalBottomSheetValue.isExpanded
    get() = this == Expanded

internal const val SHEET_FLAT_CORNER = 0
internal const val SHEET_ROUNDED_CORNER = 16
private const val START_FLAT_CORNER_POINT = 0.85

/**
 * Create a [ModalSheetState] and [remember] it.
 *
 * @param initialValue The initial value of the state.
 * @param mode modal sheet mode.[DYNAMIC] sheet height wraps content size. For not vertical scrolling or fullHeight sheetContent.
 * [EXPANDABLE] for content that could be expanded. [FULL_HEIGHT] for full screen content. if mode is [FULL_HEIGHT] and
 * initialValue is [ModalBottomSheetValue.HalfExpanded] an [IllegalArgumentException] will be thrown.
 * @param dismissOnBackPress Whether the modal should be dismiss when back press and is visible.
 * @param animationSpec The default animation that will be used to animate to a new state.
 */
@Composable
fun rememberModalSheetState(
    initialValue: ModalBottomSheetValue = Hidden,
    mode: SheetMode = EXPANDABLE,
    dismissOnBackPress: Boolean = true,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
): ModalSheetState {
    val sheetState = rememberModalBottomSheetState(
        initialValue = initialValue,
        animationSpec = animationSpec,
        skipHalfExpanded = mode == FULL_HEIGHT,
        confirmStateChange = when (mode) {
            EXPANDABLE, DYNAMIC -> { _ -> true }
            FULL_HEIGHT -> { value -> !value.isHalfExpanded }
        }
    )
    val coroutineScope = rememberCoroutineScope()
    val wrapper = remember(sheetState, coroutineScope) {
        ModalSheetState(
            layoutState = sheetState,
            mode = mode,
            coroutineScope = coroutineScope,
            hideOnBackPress = dismissOnBackPress,
        )
    }

    BackHandler(wrapper.enableHideOnBackPress) { wrapper.hide() }

    return wrapper
}

enum class SheetMode { EXPANDABLE, DYNAMIC, FULL_HEIGHT }

class ModalSheetState constructor(
    internal val layoutState: ModalBottomSheetState,
    internal val mode: SheetMode,
    private val hideOnBackPress: Boolean,
    private val coroutineScope: CoroutineScope,
) {

    val isVisible: Boolean
        get() = layoutState.isVisible
    val isHidden: Boolean
        get() = layoutState.currentValue.isHidden
    val isHalfExpanded: Boolean
        get() = layoutState.currentValue.isHalfExpanded
    val isExpanded: Boolean
        get() = layoutState.currentValue.isExpanded

    private val isFullyExpanded: Boolean
        get() = isVisible && isExpanded

    internal val showHeader: Boolean
        get() = when (mode) {
            EXPANDABLE -> isFullyExpanded
            DYNAMIC, FULL_HEIGHT -> true
        }

    internal val fillMaxSize: Boolean
        get() = mode != DYNAMIC

    internal val cornerShape: Int
        get() = if (mode == DYNAMIC || !isFullyExpanded) SHEET_ROUNDED_CORNER else SHEET_FLAT_CORNER

    internal val enableHideOnBackPress: Boolean
        get() = hideOnBackPress && isVisible

    fun hide() {
        coroutineScope.launch { layoutState.hide() }
    }

    fun show() {
        coroutineScope.launch { layoutState.show() }
    }
}