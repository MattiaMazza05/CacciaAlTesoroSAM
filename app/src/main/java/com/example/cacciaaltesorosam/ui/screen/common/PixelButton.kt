package com.example.cacciaaltesorosam.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cacciaaltesorosam.ui.theme.PressStart2P

@Composable
fun PixelButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    shadowColor: Color,
    textColor: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    val shadowOffset = 5.dp
    Box(
        modifier = modifier
            .drawBehind {
                val offsetPx = shadowOffset.toPx()
                drawRect(
                    color = shadowColor,
                    topLeft = Offset(offsetPx, offsetPx),
                    size = size
                )
            }
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontFamily = PressStart2P, fontSize = 10.sp)
    }
}