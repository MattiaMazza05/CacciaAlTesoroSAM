package com.example.cacciaaltesorosam.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import com.example.cacciaaltesorosam.PixelFont
import com.example.cacciaaltesorosam.R
import com.example.cacciaaltesorosam.media.playback.AndroidAudioPlayer
import com.example.cacciaaltesorosam.media.record.AndroidAudioRecorder
import java.io.File

@Composable
fun MasterRecordScreen(modifier: Modifier = Modifier) {
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
            Text("Tesoro", fontFamily = PixelFont)
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
    }
}