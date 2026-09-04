package com.example.cacciaaltesorosam.ui.screen.player


import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    var statoInvio by remember { mutableStateOf<Boolean?>(null) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (tesoroTrovato) "COMPLIMENTI!" else "C'ERI QUASI!",
            modifier = Modifier,
            style = MaterialTheme.typography.titleLarge,
            color = if (tesoroTrovato) PixelYellow else PixelRed
        )
        Spacer(Modifier.height(16.dp))
        Row {
            Text(nomePlayer, color = colorePlayer)
        }
        Row {

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
        Spacer(Modifier.height(40.dp))
        PixelButton(
            modifier = Modifier.fillMaxWidth(0.8f),
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

        Spacer(Modifier.height(12.dp))

        Row {
            when (statoInvio) {
                true -> Text("Inviato al Master con successo", color = PixelGreen)
                false -> Text("Errore, ripova", color = PixelRed)
                null -> {}
            }
        }
        PixelButton(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = "TORNA ALLA HOME",
            enabled = statoInvio == true,
            onClick = { onHomeClick() },
            textColor = Color.White,
            backgroundColor = if (statoInvio == true) PixelGreen else PixelPanel,
            shadowColor = if (statoInvio == true) PixelGreenShadow else PixelBorder
        )
    }
}
