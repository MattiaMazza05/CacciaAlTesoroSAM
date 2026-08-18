package com.example.cacciaaltesorosam.ui.screen.master

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.common.PixelTopBar
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow

@Composable
fun SettingScreen(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onSettingConfirmed: (String, Int) -> Unit
) {
    var gameName by remember { mutableStateOf("") }
    var isClicked by remember { mutableStateOf(0) }
    var gameDuration by remember { mutableStateOf(0) }
    Column(modifier = modifier.fillMaxSize()) {
        PixelTopBar(title = "NUOVA CACCIA", onBackClick = onBackClick)
        Spacer(modifier.height(20.dp))
        Text("NOME")
        OutlinedTextField(
            value = gameName,
            onValueChange = { gameName = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Text("SCEGLI DURATA CACCIA")
        Row {
            PixelButton(
                text = "30m",
                onClick = {
                    gameDuration = 30
                    isClicked = 1
                },
                backgroundColor = if (isClicked == 1) PixelYellow else PixelPanel,
                textColor = if (isClicked == 1) Color.Black else Color.White,
                shadowColor = if (isClicked == 1) PixelYellowShadow else PixelBorder
            )
            PixelButton(
                text = "60m",
                onClick = {
                    gameDuration = 60
                    isClicked = 2
                },
                backgroundColor = if (isClicked == 2) PixelYellow else PixelPanel,
                textColor = if (isClicked == 2) Color.Black else Color.White,
                shadowColor = if (isClicked == 2) PixelYellowShadow else PixelBorder
            )
            PixelButton(
                text = "90m",
                onClick = {
                    gameDuration = 90
                    isClicked = 3
                },
                backgroundColor = if (isClicked == 3) PixelYellow else PixelPanel,
                textColor = if (isClicked == 3) Color.Black else Color.White,
                shadowColor = if (isClicked == 3) PixelYellowShadow else PixelBorder
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        PixelButton(
            text = "INIZIA A MAPPARE",
            onClick = { onSettingConfirmed(gameName, gameDuration) },
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow,
            modifier = Modifier
                .padding(bottom = 24.dp)
        )
    }
}