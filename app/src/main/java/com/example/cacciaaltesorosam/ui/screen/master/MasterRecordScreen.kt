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
fun MasterRecordScreen(modifier: Modifier = Modifier, onPointConfirmed: (PuntoTemp) -> Unit) {
    val context = LocalContext.current
    val recorder = remember { AndroidAudioRecorder(context) }
    val player = remember { AndroidAudioPlayer(context) }
    var isRecording by remember { mutableStateOf(false) }
    var audioFile by remember { mutableStateOf<File?>(null) }
    var checked by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            audioFile = recorder.start()
            isRecording = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
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
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
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
                text = "CONFERMA PUNTO",
                onClick = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        //posizione
                    } else {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }

                    val punto = PuntoTemp(
                        audioPath = audioFile!!.absolutePath,
                        isTreasure = checked
                    )

                    onPointConfirmed(punto)

                    audioFile = null
                    isRecording = false
                    checked = false
                },
                backgroundColor = PixelYellow,
                shadowColor = PixelYellowShadow
            )
        }
    }
}