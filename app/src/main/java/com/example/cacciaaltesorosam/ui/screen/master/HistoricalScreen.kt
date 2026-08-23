package com.example.cacciaaltesorosam.ui.screen.master

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.cacciaaltesorosam.data.AppDatabase
import com.example.cacciaaltesorosam.data.GameEntity
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.common.PixelTopBar
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel

@Composable
fun HistoricalScreen(
    modifier: Modifier,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var archivio by remember { mutableStateOf(listOf<GameEntity>()) }

    LaunchedEffect(Unit) {
        val dao = AppDatabase.getDatabase(context).dao()
        archivio = dao.getAllGames()
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            PixelTopBar(title = "ARCHIVIO", onBackClick = onBackClick)
        }
        LazyColumn {
            items(archivio) { game ->
                Column {
                    PixelButton(
                        text = "Nome:${game.gameName} Durata: ${game.duration} min  Punti: ${game.pointNumber}",
                        onClick = {},
                        backgroundColor = PixelPanel,
                        shadowColor = PixelBorder,
                        textColor = Color.White
                    )
                }
            }
        }

    }
}
