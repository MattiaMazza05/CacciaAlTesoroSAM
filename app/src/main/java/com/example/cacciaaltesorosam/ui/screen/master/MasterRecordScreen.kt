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
import com.example.cacciaaltesorosam.data.Coordinate
import com.example.cacciaaltesorosam.data.PuntoCaccia
import com.example.cacciaaltesorosam.media.playback.AndroidAudioPlayer
import com.example.cacciaaltesorosam.media.record.AndroidAudioRecorder
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.common.PixelTopBar
import com.example.cacciaaltesorosam.ui.screen.common.rememberLocationTracker
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
    onPointConfirmed: (PuntoCaccia) -> Unit,
    pointNumber: Int,
    onMasterBackClick: () -> Unit,
) {

    val context = LocalContext.current
    val tracker = rememberLocationTracker()
    val recorder = remember { AndroidAudioRecorder(context) }
    val player = remember { AndroidAudioPlayer(context) }

    var isRecording by remember { mutableStateOf(false) }
    var audioFile by remember { mutableStateOf<File?>(null) }
    var checked by remember { mutableStateOf(false) }

    var masterCoord by remember { mutableStateOf<Coordinate?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val confirmButton = audioFile != null && masterCoord != null

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
            isLoading = true
            tracker.getMasterLocation(
                onSuccess = { coord ->
                    masterCoord = coord
                    isLoading = false
                },
                onError = {
                    isLoading = false
                }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        PixelTopBar(title = "CREA PUNTI E INDIZI", onBackClick = onMasterBackClick)
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
            if (isLoading) {
                Text("CARICAMENTO", color = PixelYellow)
            } else if (masterCoord?.latitude == null) {
                Text("NULL", color = PixelRed)
            } else {
                Text("${masterCoord?.latitude}", color = PixelGreen)
            }

            Spacer(Modifier.width(8.dp))

            Text("long: ")
            if (isLoading) {
                Text("CARICAMENTO", color = PixelYellow)
            } else if (masterCoord?.longitude == null) {
                Text("NULL", color = PixelRed)
            } else {
                Text("${masterCoord?.longitude}", color = PixelGreen)
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
                    val hasLocPermission = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasLocPermission) {
                        isLoading = true
                        tracker.getMasterLocation(
                            onSuccess = { coord ->
                                masterCoord = coord
                                isLoading = false
                            },
                            onError = {
                                isLoading = false
                            }
                        )
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
                    val loc = masterCoord ?: return@PixelButton
                    val audio = audioFile ?: return@PixelButton
                    val punto = PuntoCaccia(
                        audioPath = audio.absolutePath,
                        isTreasure = checked,
                        latitude = loc.latitude,
                        longitude = loc.longitude
                    )
                    onPointConfirmed(punto)
                    audioFile = null
                    isRecording = false
                    checked = false
                    masterCoord = null
                },
                backgroundColor = if (checked) PixelGreen else PixelYellow,
                shadowColor = if (checked) PixelGreenShadow else PixelYellowShadow
            )
        }
    }
}