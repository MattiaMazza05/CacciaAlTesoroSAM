package com.example.cacciaaltesorosam.ui.screen.master

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID


@SuppressLint("MissingPermission")
@Composable
fun SendGammeViaBluetooth(modifier: Modifier, masterNick: String) {
    val bta = BluetoothAdapter.getDefaultAdapter()
    var foundDevices by remember { mutableStateOf(listOf<BluetoothDevice>()) }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action: String? = intent?.action
                when (action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device: BluetoothDevice? =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        if (device != null && foundDevices.none { it.address == device.address }) {
                            foundDevices = foundDevices + device
                        }
                    }
                }
            }
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
        val started = bta.startDiscovery()
        Log.d("BLUETOOTH_DISCOVERY", "Discovery avviata: $started")
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            "DISPOSITIVI COMPATIBILI",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier.height(30.dp))
        LazyColumn {
            items(foundDevices) { device ->
                PixelButton(
                    text = device.name ?: device.address,
                    onClick = {
                        pairConnectionBluetooth(masterNick, bta)
                    },
                    backgroundColor = PixelPanel,
                    shadowColor = PixelBorder,
                    textColor = Color.White
                )
            }
        }
        PixelButton(
            text = "ASPETTA CONNESSIONI (MASTER)",
            onClick = { pairConnectionBluetooth(masterNick, bta) },
            backgroundColor = PixelPanel,
            shadowColor = PixelBorder,
            textColor = Color.White
        )
    }
}

@SuppressLint("MissingPermission")
fun pairConnectionBluetooth(masterNick: String, bta: BluetoothAdapter) {
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
                Log.d("CONNECTED", "CONNESSIONE AVVENUTAA")
            }
        }
    }
}


