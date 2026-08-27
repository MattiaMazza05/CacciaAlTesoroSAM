package com.example.cacciaaltesorosam.ui.screen.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.data.DistanzaStato
import com.example.cacciaaltesorosam.data.Game
import com.example.cacciaaltesorosam.ui.screen.common.GameTopBar
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelGreen
import com.example.cacciaaltesorosam.ui.theme.PixelGreenShadow
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow
import kotlinx.coroutines.delay


@Composable
fun InGame(modifier: Modifier, game: Game) {
    var tappaAttuale by remember { mutableStateOf(1) }
    var secondiRimanenti by remember { mutableStateOf(game.duration * 60) }
    var nextButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (secondiRimanenti > 0) {
            delay(1000)
            secondiRimanenti--
        }
    }
    val minuti = secondiRimanenti / 60
    val secondi = secondiRimanenti % 60
    val tempoRimanete = "%02d:%02d".format(minuti, secondi)
    Column(modifier.fillMaxSize()) {
        GameTopBar(
            tappaAttuale,
            game.punti.size,
            tempoRimanete,
            {}
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DistanzaAnimazione(DistanzaStato.LONTANO)
            PixelButton(
                text = "RIPRODUCI INDIZIO",
                onClick = {},
                backgroundColor = PixelGreen,
                shadowColor = PixelGreenShadow
            )
        }
        PixelButton(
            "VAI AL PROSSIMO",
            enabled = nextButton,
            onClick = {},
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}