package com.example.cacciaaltesorosam.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}

fun Color.toHex(): String {
    val argb = this.toArgb()
    return String.format("#%08X", argb)
}