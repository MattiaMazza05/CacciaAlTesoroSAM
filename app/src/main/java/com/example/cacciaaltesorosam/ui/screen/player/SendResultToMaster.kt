package com.example.cacciaaltesorosam.ui.screen.player

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.cacciaaltesorosam.data.toHex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.IOException
import java.util.UUID

val RESULT_SERVICE_UUID: UUID = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8")

@SuppressLint("MissingPermission")
fun sendResultToMaster(
    context: Context,
    tempoTrascorso: Int,
    nomePlayer: String,
    colorePlayer: Color,
    tesoroTrovato: Boolean,
    masterDevice: BluetoothDevice,
    onResult: (Boolean) -> Unit
) {
    val hasPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.BLUETOOTH_CONNECT
    ) == PackageManager.PERMISSION_GRANTED
    if (!hasPermission) {
        Log.e("ERRORE", "Permesso BLUETOOTH_CONNECT mancante")
        onResult(false)
        return
    }

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val socketPlayer = masterDevice.createRfcommSocketToServiceRecord(RESULT_SERVICE_UUID)
            socketPlayer.connect()
            val dataOut = DataOutputStream(socketPlayer.outputStream)
            val jsonObject = JSONObject().apply {
                put("nomePlayer", nomePlayer)
                put("colorePlayer", colorePlayer.toHex())
                put("tempoTrascorso", tempoTrascorso)
                put("tesoroTrovato", tesoroTrovato)
            }
            val resultJSON = jsonObject.toString()
            dataOut.writeUTF(resultJSON)
            dataOut.flush()
            kotlinx.coroutines.delay(300)
            socketPlayer.close()
            onResult(true)
        } catch (e: IOException) {
            Log.e("ERRORE", "errore player bluetooth", e)
            onResult(false)
        }
    }
}

