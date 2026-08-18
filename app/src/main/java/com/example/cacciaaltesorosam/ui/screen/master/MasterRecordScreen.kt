package com.example.cacciaaltesorosam.ui.screen.master

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
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
import androidx.core.content.ContextCompat
import com.example.cacciaaltesorosam.data.PuntoTemp
import com.example.cacciaaltesorosam.media.playback.AndroidAudioPlayer
import com.example.cacciaaltesorosam.media.record.AndroidAudioRecorder
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.common.locationUpdate
import com.example.cacciaaltesorosam.ui.screen.common.rememberMasterLocation
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelGreen
import com.example.cacciaaltesorosam.ui.theme.PixelGreenShadow
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import com.example.cacciaaltesorosam.ui.theme.PixelRed
import com.example.cacciaaltesorosam.ui.theme.PixelRedShadow
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow
import java.io.File

@Composable
fun MasterRecordScreen(
    modifier: Modifier = Modifier,
    onPointConfirmed: (PuntoTemp) -> Unit,
    pointNumber: Int
) {
    val context = LocalContext.current
    val recorder = remember { AndroidAudioRecorder(context) }
    val player = remember { AndroidAudioPlayer(context) }
    var isRecording by remember { mutableStateOf(false) }
    var audioFile by remember { mutableStateOf<File?>(null) }
    var checked by remember { mutableStateOf(false) }
    val hasPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val currentLocationState = rememberMasterLocation(context)
    val currentLocation = currentLocationState.value
    val confirmButton = audioFile != null && currentLocation != null
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            audioFile = recorder.start()
            isRecording = true
        }
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locationUpdate()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Text(
            "CREAZIONE CACCIA IN CORSO",
            style = MaterialTheme.typography.titleLarge
        )
        Row {
            Text("PUNTO N: ")
            Text("${pointNumber}", color = PixelGreen)
        }
        Row {
            Text("INDIZIO AUDIO: ")
            if (audioFile == null) {
                Text("NON PRONTO", color = PixelRed)
            } else {
                Text("Pronto", color = PixelGreen)
            }
        }
        Row {
            Text("POSIZIONE: ")
            Text("lat: ")
            if (currentLocation?.latitude == null) {
                Text("NULL", color = PixelRed)
            } else {
                Text("${currentLocation.latitude}", color = PixelGreen)
            }
            Text("long: ")
            if (currentLocation?.longitude == null) {
                Text("NULL", color = PixelRed)
            } else {
                Text("${currentLocation.longitude}", color = PixelGreen)
            }
        }
        PixelButton(
            text = if (isRecording) "STOP" else "REGISTRA",
            onClick = {
                if (isRecording) {
                    recorder.stop()
                    isRecording = false
                } else {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        audioFile = recorder.start()
                        isRecording = true
                    } else {
                        audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            },
            backgroundColor = if (isRecording) PixelRed else PixelGreen,
            textColor = if (isRecording) Color.Black else Color.White,
            shadowColor = if (isRecording) PixelRedShadow else PixelGreenShadow
        )
        Spacer(Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PixelButton(
                text = "RIASCOLTA",
                onClick = {
                    player.playFile(audioFile ?: return@PixelButton)
                },
                backgroundColor = PixelPanel,
                textColor = Color.White,
                shadowColor = PixelBorder
            )
            Spacer(Modifier.width(14.dp))
            PixelButton(
                text = "STOP",
                onClick = {
                    player.stop()
                },
                backgroundColor = PixelPanel,
                textColor = Color.White,
                shadowColor = PixelBorder
            )
        }
        Spacer(Modifier.width(14.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PixelButton(
                text = "FISSA POSIZIONE",
                onClick = {
                    if (hasPermission) {
                        locationUpdate()
                    } else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                backgroundColor = PixelPanel,
                shadowColor = PixelBorder,
                textColor = Color.White
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tesoro")
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PixelButton(
                text = if (checked) "CONCLUDI E SALVA" else "SALVA E VAI AL PROSSIMO",
                enabled = confirmButton,
                onClick = {
                    val loc = currentLocation ?: return@PixelButton
                    val punto = PuntoTemp(
                        audioPath = audioFile!!.absolutePath,
                        isTreasure = checked,
                        latitude = loc.latitude,
                        longitude = loc.longitude
                    )

                    onPointConfirmed(punto)
                    audioFile = null
                    isRecording = false
                    checked = false
                    currentLocationState.value = null
                },
                backgroundColor = if (checked) PixelGreen else PixelYellow,
                shadowColor = if (checked) PixelGreenShadow else PixelYellowShadow
            )
        }
    }
}