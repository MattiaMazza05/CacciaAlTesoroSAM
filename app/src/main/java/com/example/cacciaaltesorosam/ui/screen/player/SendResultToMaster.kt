package com.example.cacciaaltesorosam.ui.screen.player

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.util.UUID

val RESULT_SERVICE_UUID: UUID = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8")
@Composable
fun sendResultToMaster(
    modifier: Modifier,
    tempoTrascorso: Int,
    nomePlayer: String,
    colorePlayer: Color,
    tesoroTrovato: Boolean
) {
    val context = LocalContext.current
    var hasConnectPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasConnectPermission = isGranted
    }
    if(hasConnectPermission){
        try {

        }catch ()
    }else{
        permissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
    }
}