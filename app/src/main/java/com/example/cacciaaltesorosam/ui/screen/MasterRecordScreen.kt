package com.example.cacciaaltesorosam.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.cacciaaltesorosam.R
import com.example.cacciaaltesorosam.data.PuntoTemp
import com.example.cacciaaltesorosam.media.playback.AndroidAudioPlayer
import com.example.cacciaaltesorosam.media.record.AndroidAudioRecorder
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
        LargeFloatingActionButton(onClick = {
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
        }) {
            Icon(
                painter = if (isRecording) painterResource(R.drawable.baseline_stop_24) else painterResource(
                    R.drawable.twotone_mic_24
                ),
                contentDescription = if (isRecording) "Stop recording" else "Start recording"
            )
        }

        LargeFloatingActionButton(
            onClick = {
                player.playFile(audioFile ?: return@LargeFloatingActionButton)
            }
        ) {
            Icon(
                painter = painterResource(
                    R.drawable.round_play_arrow_24
                ),
                "Start playing"
            )
        }

        LargeFloatingActionButton(
            onClick = {
                player.stop()
            }
        ) {
            Icon(
                painter = painterResource(
                    R.drawable.round_pause_24
                ),
                "Stop playing"
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
            PixelButtonConfirmed {
                val punto = PuntoTemp(
                    audioPath = audioFile!!.absolutePath,
                    isTreasure = checked
                )

                onPointConfirmed(punto)

                audioFile = null
                isRecording = false
                checked = false
            }
        }
    }
}

@Composable
fun PixelButtonConfirmed(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Anima la scala: se premuto scende a 0.95 (95%), altrimenti torna a 1f
    val targetScale = if (isPressed) 0.95f else 1f
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 400f), // Effetto "gommoso/arcade"
        label = "buttonScale"
    )
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(80.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.conferma_punto_bottone),
            contentDescription = "Conferma Punto",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Fit
        )
    }
}