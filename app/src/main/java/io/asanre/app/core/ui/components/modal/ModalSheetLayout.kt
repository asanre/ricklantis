package io.asanre.app.core.ui.components.modal

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.asanre.app.core.ui.theme.RicklantisTheme

@Composable
fun ModalBottomSheetContainer(
    modifier: Modifier = Modifier,
    modal: @Composable() () -> Unit,
    content: @Composable() () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()
        modal()
    }
}

@Composable
fun ModalBottomSheet(
    state: ModalSheetState,
    modifier: Modifier = Modifier,
    sheetContent: @Composable() () -> Unit,
) {
    val sheetCorner by animateDpAsState((state.cornerShape).dp)

    ModalBottomSheetLayout(modifier = modifier,
        sheetState = state.layoutState,
        sheetContent = {
            AnimatedContent(targetState = state.showHeader) { showHeader ->
                if (showHeader.not()) {
                    SheetDrawerIndicator()
                }
            }
            if (state.fillMaxSize) {
                Box(modifier = Modifier.fillMaxSize(), content = { sheetContent() })
            } else {
                sheetContent()
            }
        },
        sheetShape = RoundedCornerShape(topEnd = sheetCorner, topStart = sheetCorner),
        content = {})
}

@Suppress("MagicNumber", "ForEachOnRange")
@Preview
@Composable
private fun ModalPreview() {
    RicklantisTheme {
        val state = rememberModalSheetState(mode = SheetMode.FULL_HEIGHT)

        ModalBottomSheetContainer(modal = {
            ModalBottomSheet(
                state = state,
                modifier = Modifier.padding(top = 40.dp),
                sheetContent = {
                    Column {
                        (1..10).forEach {
                            Text("Item $it")
                        }
                    }
                })
        }) {
            OutlinedButton(onClick = { state.show() }) {
                Text(text = "Show modal")
            }
        }
    }
}