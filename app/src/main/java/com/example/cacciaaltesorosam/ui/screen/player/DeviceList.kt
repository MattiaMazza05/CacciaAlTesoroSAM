package com.example.cacciaaltesorosam.ui.screen.player

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.cacciaaltesorosam.data.Game
import com.example.cacciaaltesorosam.data.ParseGame
import com.example.cacciaaltesorosam.data.PuntoCaccia
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.master.GAME_SERVICE_UUID
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.File
import java.io.IOException


@Composable
fun DeviceList(
    modifier: Modifier,
    onSelectClick: (Game) -> Unit
) {
    val bta = BluetoothAdapter.getDefaultAdapter()
    var foundDevices by remember { mutableStateOf(listOf<BluetoothDevice>()) }
    val context = LocalContext.current


    var hasScanPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasConnectPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results: Map<String, Boolean> ->
        hasScanPermission = results[Manifest.permission.BLUETOOTH_SCAN] ?: false
        hasConnectPermission = results[Manifest.permission.BLUETOOTH_CONNECT] ?: false
    }

    LaunchedEffect(Unit) {
        if (!hasScanPermission || !hasConnectPermission) {
            permissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        }
    }
    DisposableEffect(hasScanPermission) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d("BT_RAW", "Ricevuto broadcast: ${intent?.action}")
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
        context.registerReceiver(
            receiver,
            filter
        )
        if (hasScanPermission) {
            val started = bta.startDiscovery()
            Log.d("BLUETOOTH_DISCOVERY", "Discovery avviata: $started")
        }
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(
                "DISPOSITIVI COMPATIBILI",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier.height(30.dp))
        LazyColumn {
            items(foundDevices) { device ->
                PixelButton(
                    text = device.name ?: device.address,
                    onClick = {
                        if (hasConnectPermission) {
                            CoroutineScope(Dispatchers.IO).launch {
                                bta.cancelDiscovery()
                                try {
                                    val socket =
                                        device.createRfcommSocketToServiceRecord(GAME_SERVICE_UUID)
                                    socket.connect()
                                    val inputStream = DataInputStream(socket.inputStream)
                                    val buffer = inputStream.readUTF()
                                    Log.d("BLUETOOTH_RECEIVE", buffer)
                                    val gioco = ParseGame(buffer)
                                    val puntiAggiornati = mutableListOf<PuntoCaccia>()
                                    gioco.punti.forEachIndexed { index, punto ->
                                        val dimensione = inputStream.readInt()
                                        val bytesAudio = ByteArray(dimensione)
                                        inputStream.readFully(bytesAudio)
                                        val file = File(context.filesDir, "punto_$index.m4a")
                                        file.writeBytes(bytesAudio)
                                        val puntoAggiornato =
                                            punto.copy(audioPath = file.absolutePath)
                                        puntiAggiornati.add(puntoAggiornato)
                                    }
                                    val giocoAggiornato = gioco.copy(punti = puntiAggiornati)
                                    onSelectClick(giocoAggiornato)
                                } catch (e: IOException) {
                                    Log.e("BLUETOOTH_RECEIVE", "Connessione fallita", e)
                                }
                            }
                        }
                    },
                    backgroundColor = PixelPanel,
                    shadowColor = PixelBorder,
                    textColor = Color.White
                )
            }
        }
    }

}