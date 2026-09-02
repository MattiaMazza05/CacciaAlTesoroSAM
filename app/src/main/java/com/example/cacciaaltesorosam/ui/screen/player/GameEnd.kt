package com.example.cacciaaltesorosam.ui.screen.player


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelGreen
import com.example.cacciaaltesorosam.ui.theme.PixelRed
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow

@Composable
fun GameEnd(
    modifier: Modifier,
    tempoTrascorso: Int,
    nomePlayer: String,
    colorePlayer: Color,
    tesoroTrovato: Boolean,
    onHomeClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            if (tesoroTrovato) "COMPLIMENTI!" else "C'ERI QUASI!",
            style = MaterialTheme.typography.titleLarge,
            color = if (tesoroTrovato) PixelYellow else PixelRed
        )
        Row {
            Text(nomePlayer, color = colorePlayer)
            if (tesoroTrovato) {
                Text(" hai completato la caccia in: ")
                Text(
                    "%02d:%02d".format(tempoTrascorso / 60, tempoTrascorso % 60),
                    color = PixelGreen
                )
            } else {
                Text("Tempo scaduto, non hai trovato il tesoro")
            }
        }
        //eventuale traccia gps
        PixelButton(
            text = "CONDIVIDI AL MASTER E TORNA ALLA HOME",
            onClick = { onHomeClick() },
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow
        )
    }
}
