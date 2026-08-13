package com.example.cacciaaltesorosam.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val PixelColorScheme = darkColorScheme(
    background = PixelBgDark,
    surface = PixelPanel,
    primary = PixelYellow,
    secondary = PixelGreen,
    error = PixelRed,
    onBackground = Color.White,
    onSurface = PixelTextMuted
)
private val PixelShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(0.dp),
    medium = RoundedCornerShape(0.dp),
    large = RoundedCornerShape(0.dp),
    extraLarge = RoundedCornerShape(0.dp)
)

@Composable
fun CacciaAlTesoroSAMTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PixelColorScheme,
        typography = Typography,
        shapes = PixelShapes,
        content = content
    )
}