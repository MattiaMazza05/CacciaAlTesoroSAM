package com.example.cacciaaltesorosam.ui.screen.master

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.data.RisultatoGame
import com.example.cacciaaltesorosam.data.StatoConnessione
import com.example.cacciaaltesorosam.data.toColor
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.player.RESULT_SERVICE_UUID
import com.example.cacciaaltesorosam.ui.theme.CacciaAlTesoroSAMTheme
import com.example.cacciaaltesorosam.ui.theme.PixelBlue
import com.example.cacciaaltesorosam.ui.theme.PixelGreen
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import com.example.cacciaaltesorosam.ui.theme.PixelRed
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataInputStream
import java.io.IOException

@Composable
fun PlayerInteraction(
    modifier: Modifier,
    stato: StatoConnessione,
    onRiceviClick: () -> Unit,
    risultati: List<RisultatoGame>
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            when (stato) {
                StatoConnessione.PRONTO -> "INVIANDO LA PARTITA"
                StatoConnessione.ATTESA -> "ATTENDI CHE I PLAYER GIOCHINO"
                StatoConnessione.RICEZIONE -> "OTTENENDO RISULTATI"
            },
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        StatoPartitaAnimazione(stato)
        Spacer(modifier = Modifier.height(24.dp))
        PixelButton(
            text = "RICEVI RISULTATI",
            onClick = {
                onRiceviClick()
            },
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(risultati.sortedBy { it.tempoTrascorso }) { risultato ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .background(PixelPanel).padding(12.dp)){
                Row {
                    Text("${risultato.nomePlayer} - ", color = risultato.colorePlayer.toColor())
                    Text("Tesoro: ")
                    if (risultato.tesoroTrovato) {
                        Text("TROVATO", color = PixelGreen)
                    } else {
                        Text("NON TROVATO", color = PixelRed)
                    }
                }
                Row {
                    Text("TEMPO TRASCORSO: ")
                    Text("${risultato.tempoTrascorso} sec", color = PixelBlue)
                }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun pairPlayer(
    bta: BluetoothAdapter, onRisultatoRicevuto: (RisultatoGame) -> Unit
) {
    val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
        bta.listenUsingRfcommWithServiceRecord("Master risultati", RESULT_SERVICE_UUID)
    }
    CoroutineScope(Dispatchers.IO).launch {
        var loop = true
        while (loop) {
            val socket: BluetoothSocket? = try {
                mmServerSocket?.accept()
            } catch (e: IOException) {
                Log.e(TAG, "Socket's accept() method failed", e)
                loop = false
                null
            }
            if (socket != null) {
                try {
                    val dataIn = DataInputStream(socket.inputStream)
                    val json = JSONObject(dataIn.readUTF())
                    onRisultatoRicevuto(
                        RisultatoGame(
                            nomePlayer = json.getString("nomePlayer"),
                            colorePlayer = json.getString("colorePlayer"),
                            tempoTrascorso = json.getInt("tempoTrascorso"),
                            tesoroTrovato = json.getBoolean("tesoroTrovato")
                        )
                    )
                    socket.close()
                } catch (e: IOException) {
                    Log.e("RISULTATI", "Lettura fallita", e)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerInteractionPreview() {
    val risultatiFinti = listOf(
        RisultatoGame(
            nomePlayer = "Mattia",
            colorePlayer = "#FF43A047", // verde
            tempoTrascorso = 245,
            tesoroTrovato = true
        ),
        RisultatoGame(
            nomePlayer = "Luca",
            colorePlayer = "#FFE53935", // rosso
            tempoTrascorso = 312,
            tesoroTrovato = true
        ),
        RisultatoGame(
            nomePlayer = "Giulia",
            colorePlayer = "#FF1E88E5", // blu
            tempoTrascorso = 600,
            tesoroTrovato = false
        )
    )

    CacciaAlTesoroSAMTheme {
        PlayerInteraction(
            modifier = Modifier,
            stato = StatoConnessione.RICEZIONE,
            risultati = risultatiFinti,
            onRiceviClick = {}
        )
    }
}