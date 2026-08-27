package com.example.cacciaaltesorosam.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import com.example.cacciaaltesorosam.ui.theme.PixelYellow

@Composable
fun GameTopBar(
    tappaAttuale: Int,
    tappaTotali: Int,
    tempoRimanente: String,
    onAbbandonaClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(PixelPanel)
            .padding(16.dp)
            .height(48.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(
                "TAPPA:"

            )
            Text("$tappaAttuale/$tappaTotali", color = PixelYellow)
        }
        Text(
            tempoRimanente,
            modifier = Modifier.align(Alignment.Center)
        )
        Text(
            "ABBANDONA",
            color = Color.Red,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { onAbbandonaClick() }
        )
    }
}