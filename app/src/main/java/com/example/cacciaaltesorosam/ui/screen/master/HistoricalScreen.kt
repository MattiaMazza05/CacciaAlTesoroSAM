package com.example.cacciaaltesorosam.ui.screen.master

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.cacciaaltesorosam.ui.screen.common.PixelTopBar

@Composable
fun HistoricalScreen(
    modifier: Modifier,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            PixelTopBar(title = "ARCHIVIO", onBackClick = onBackClick)
        }

    }
}