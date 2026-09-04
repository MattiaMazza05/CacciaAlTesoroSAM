package com.example.cacciaaltesorosam.ui.screen.master

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.cacciaaltesorosam.data.StatoConnessione
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.util.UUID

val GAME_SERVICE_UUID: UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")

@SuppressLint("MissingPermission")
@Composable
fun SendGammeViaBluetooth(
    modifier: Modifier,
    masterNick: String,
    gameJSON: String?,
    audioPaths: List<String>,
    onWaitClick: () -> Unit,
    onStatoChange: (StatoConnessione) -> Unit
) {
    val bta = BluetoothAdapter.getDefaultAdapter()
    val context = LocalContext.current
    var hasConnectPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val connectPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGaranted ->
        hasConnectPermission = isGaranted
        if (isGaranted) {
            pairConnectionBluetooth(masterNick, bta, gameJSON, audioPaths) { successo ->
                onStatoChange(if (successo) StatoConnessione.ATTESA else StatoConnessione.PRONTO)
            }
        }
    }
    LaunchedEffect(Unit) {
        if (!hasConnectPermission) {
            connectPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        }
    }
    val discoverableLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("BLUETOOTH_DISCOVERABLE", "Risultato: ${result.resultCode}")
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "PRONTO A CONDIVIDERE",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier.height(12.dp))
        Text(
            "Il dispositivo diventerà visibile per 2 minuti: assicurati che i player abbiano il Bluetooth attivo e siano nelle vicinanze.",
            textAlign = TextAlign.Center
        )
        Spacer(modifier.height(32.dp))

        PixelButton(
            text = "TRASMETTI PARTITA",
            onClick = {
                onStatoChange(StatoConnessione.PRONTO)
                if (hasConnectPermission) {
                    pairConnectionBluetooth(
                        masterNick,
                        bta,
                        gameJSON,
                        audioPaths
                    ) { successo -> onStatoChange(if (successo) StatoConnessione.ATTESA else StatoConnessione.PRONTO) }
                } else {
                    connectPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                }
                val discoverableIntent =
                    Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120)
                    }
                discoverableLauncher.launch(discoverableIntent)
                onWaitClick()
            },
            backgroundColor = PixelPanel,
            shadowColor = PixelBorder,
            textColor = Color.White,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}


@SuppressLint("MissingPermission")
fun pairConnectionBluetooth(
    masterNick: String,
    bta: BluetoothAdapter,
    gameJSON: String?,
    audioPaths: List<String>,
    onResult: (Boolean) -> Unit
) {
    Log.d("BLUETOOTH_MASTER", "pairConnectionBluetooth chiamata")
    val masterUUID: UUID = GAME_SERVICE_UUID
    val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
        bta.listenUsingRfcommWithServiceRecord("Master: ${masterNick}", masterUUID)
    }
    CoroutineScope(Dispatchers.IO).launch {
        var loop = true
        while (loop) {
            Log.d("BLUETOOTH_MASTER", "In attesa su accept()...")
            val socket: BluetoothSocket? = try {
                mmServerSocket?.accept()
            } catch (e: IOException) {
                Log.e(TAG, "Socket's accept() method failed", e)
                loop = false
                null
            }
            if (socket != null) {
                gameJSON?.let { string -> sendGame(socket, string, audioPaths) }
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }
}


fun sendGame(mmSocket: BluetoothSocket, gameJSON: String, audioPaths: List<String>) {
    val dataOut = DataOutputStream(mmSocket.outputStream)

    try {
        dataOut.writeUTF(gameJSON)
        audioPaths.forEach {
            val bytes = File(it).readBytes()
            dataOut.writeInt(bytes.size)
            dataOut.write(bytes)
        }
        dataOut.flush()
    } catch (e: IOException) {
        Log.e(TAG, "ERRORE", e)
    }

}
