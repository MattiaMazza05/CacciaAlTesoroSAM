package com.example.cacciaaltesorosam.ui.screen.master

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel


@SuppressLint("MissingPermission")
@Composable
fun sendGammeViaBluetooth() {
    val bta = BluetoothAdapter.getDefaultAdapter()
    var foundDevices by remember { mutableStateOf(listOf<BluetoothDevice>()) }
    val context = LocalContext.current
    val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Manifest.permission.BLUETOOTH_CONNECT
    } else {
        Manifest.permission.BLUETOOTH
    }
    val isPermissionGranted = ContextCompat.checkSelfPermission(
        context,
        requiredPermission
    ) == PackageManager.PERMISSION_GRANTED
    if (isPermissionGranted) {
        val pairedDevice: Set<BluetoothDevice>? = bta.bondedDevices
        pairedDevice?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address
            Log.d("BLUETOOTH", "Nome: ${deviceName}, MAC: ${deviceHardwareAddress}")
        }
    }


    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action: String? = intent?.action
                when (action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device: BluetoothDevice? =
                            intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        if (device != null && foundDevices.none { it.address == device.address }) {
                            foundDevices = foundDevices + device
                        }
                    }
                }
            }
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        val started = bta.startDiscovery()
        Log.d("BLUETOOTH_DISCOVERY", "Discovery avviata: $started")
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
    LazyColumn {
        items(foundDevices) { device ->
            PixelButton(
                text = device.name ?: device.address,
                onClick = {
                },
                backgroundColor = PixelPanel,
                shadowColor = PixelBorder,
                textColor = Color.White
            )
        }
    }
}