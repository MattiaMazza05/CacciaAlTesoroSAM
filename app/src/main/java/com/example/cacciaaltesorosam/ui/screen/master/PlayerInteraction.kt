package com.example.cacciaaltesorosam.ui.screen.master

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cacciaaltesorosam.data.RisultatoGame
import com.example.cacciaaltesorosam.data.StatoConnessione
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.player.RESULT_SERVICE_UUID
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataInputStream
import java.io.IOException

@Composable
fun PlayerInteraction(modifier: Modifier, stato: StatoConnessione, onRiceviClick: () -> Unit) {
    Column(modifier.fillMaxSize()) {
        Row {
            when (stato) {
                StatoConnessione.PRONTO -> Text("INVIANDO LA PARTITA")
                StatoConnessione.ATTESA -> Text("ATTENDI CHE I PLAYER GIOCHINO")
                StatoConnessione.RICEZIONE -> Text("OTTENENDO RISULTATI")
            }
        }
        StatoPartitaAnimazione(stato)
        PixelButton(
            text = "RICEVI RISULTATI",
            onClick = {
                onRiceviClick()
            },
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow
        )
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