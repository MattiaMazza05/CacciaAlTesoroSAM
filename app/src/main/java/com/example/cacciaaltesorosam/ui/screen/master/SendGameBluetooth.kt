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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.util.UUID


@SuppressLint("MissingPermission")
@Composable
fun SendGammeViaBluetooth(modifier: Modifier, masterNick: String, gameByte: ByteArray?) {
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
            pairConnectionBluetooth(masterNick, bta, gameByte)
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
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            "DISPOSITIVI COMPATIBILI",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier.height(30.dp))
        PixelButton(
            text = "ATTENDI PLAYER",
            onClick = {
                val discoverableIntent =
                    Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120)
                    }
                discoverableLauncher.launch(discoverableIntent)
                if (hasConnectPermission) {
                    pairConnectionBluetooth(masterNick, bta, gameByte)
                } else {
                    connectPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                }
            },
            backgroundColor = PixelPanel,
            shadowColor = PixelBorder,
            textColor = Color.White
        )
    }
}


@SuppressLint("MissingPermission")
fun pairConnectionBluetooth(masterNick: String, bta: BluetoothAdapter, gameByte: ByteArray?) {
    val masterUUID: UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
        bta.listenUsingRfcommWithServiceRecord("Master: ${masterNick}", masterUUID)
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
            socket?.also {
                gameByte?.let { bytes -> sendGame(it, bytes) }
            }
        }
    }
}


fun sendGame(mmSocket: BluetoothSocket, game: ByteArray) {
    val mmOutStream: OutputStream = mmSocket.outputStream

    try {
        mmOutStream.write(game)
        mmOutStream.flush()
    } catch (e: IOException) {
        Log.e(TAG, "ERRORE", e)
    }

}

