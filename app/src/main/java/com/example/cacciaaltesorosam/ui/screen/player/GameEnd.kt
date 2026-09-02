package com.example.cacciaaltesorosam.ui.screen.player


import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelGreen
import com.example.cacciaaltesorosam.ui.theme.PixelGreenShadow
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
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
    onHomeClick: () -> Unit,
    masterDevice: BluetoothDevice?
) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
        var statoInvio by remember { mutableStateOf<Boolean?>(null) }
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
            text = "CONDIVIDI AL MASTER",
            onClick = {
                val device = masterDevice
                if (device != null) {
                    sendResultToMaster(
                        context,
                        tempoTrascorso,
                        nomePlayer,
                        colorePlayer,
                        tesoroTrovato,
                        device
                    ) { risultato ->
                        statoInvio = risultato
                    }
                }
            },
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow
        )

        Row {
            when (statoInvio) {
                true -> Text("Inviato al MAster con successo", color = PixelGreen)
                false -> Text("Errore, ripova", color = PixelRed)
                null -> {}
            }
        }
        PixelButton(
            "TORNA ALLA HOME",
            enabled = statoInvio == true,
            onClick = { onHomeClick() },
            backgroundColor = if (statoInvio == true) PixelGreen else PixelPanel,
            shadowColor = if (statoInvio == true) PixelGreenShadow else PixelBorder
        )
    }
}
