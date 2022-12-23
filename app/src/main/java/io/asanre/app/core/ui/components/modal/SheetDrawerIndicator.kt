package io.asanre.app.core.ui.components.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


private const val SHEET_DRAWER_WIDTH = 40
private const val SHEET_DRAWER_HEIGHT = 2
private const val SHEET_DRAWER_ROUNDED_CORNER = 8
private const val SHEET_DRAWER_PADDING = 8

@Composable
fun SheetDrawerIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = SHEET_DRAWER_PADDING.dp)
                .size(SHEET_DRAWER_WIDTH.dp, SHEET_DRAWER_HEIGHT.dp)
                .clip(RoundedCornerShape(SHEET_DRAWER_ROUNDED_CORNER.dp))
                .background(Color.LightGray)
        )
    }
}