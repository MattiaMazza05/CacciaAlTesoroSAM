package com.example.cacciaaltesorosam.ui.screen.master

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.common.PixelTopBar
import com.example.cacciaaltesorosam.ui.theme.CacciaAlTesoroSAMTheme
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow

@Composable
fun SettingScreen(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onSettingConfirmed: (String, Int, String) -> Unit
) {
    var gameName by remember { mutableStateOf("") }
    var isClicked by remember { mutableStateOf(0) }
    var gameDuration by remember { mutableStateOf(0) }
    var masterNick by remember { mutableStateOf("") }
    var buttonEnabeld by remember { mutableStateOf(false) }
    if(gameName != "" && gameDuration != 0 && masterNick != "") buttonEnabeld = true
    Column(modifier = modifier.fillMaxSize()) {
        PixelTopBar(title = "NUOVA CACCIA", onBackClick = onBackClick)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier.height(20.dp))

            Text("NOME CACCIA")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = gameName,
                onValueChange = { gameName = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text("NOME MASTER")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = masterNick,
                onValueChange = { masterNick = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text("SCEGLI DURATA CACCIA")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PixelButton(
                    text = "TEST",
                    onClick = {
                        gameDuration = 1
                        isClicked = 0
                    },
                    backgroundColor = if (isClicked == 1) PixelYellow else PixelPanel,
                    textColor = if (isClicked == 1) Color.Black else Color.White,
                    shadowColor = if (isClicked == 1) PixelYellowShadow else PixelBorder,
                    modifier = Modifier.weight(1f)
                )
                PixelButton(
                    text = "30m",
                    onClick = {
                        gameDuration = 30
                        isClicked = 1
                    },
                    backgroundColor = if (isClicked == 1) PixelYellow else PixelPanel,
                    textColor = if (isClicked == 1) Color.Black else Color.White,
                    shadowColor = if (isClicked == 1) PixelYellowShadow else PixelBorder,
                    modifier = Modifier.weight(1f)
                )
                PixelButton(
                    text = "60m",
                    onClick = {
                        gameDuration = 60
                        isClicked = 2
                    },
                    backgroundColor = if (isClicked == 2) PixelYellow else PixelPanel,
                    textColor = if (isClicked == 2) Color.Black else Color.White,
                    shadowColor = if (isClicked == 2) PixelYellowShadow else PixelBorder,
                    modifier = Modifier.weight(1f)
                )
                PixelButton(
                    text = "90m",
                    onClick = {
                        gameDuration = 90
                        isClicked = 3
                    },
                    backgroundColor = if (isClicked == 3) PixelYellow else PixelPanel,
                    textColor = if (isClicked == 3) Color.Black else Color.White,
                    shadowColor = if (isClicked == 3) PixelYellowShadow else PixelBorder,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            PixelButton(
                modifier = Modifier.fillMaxWidth(0.8f),
                enabled = buttonEnabeld,
                text = "INIZIA A MAPPARE",
                onClick = {
                    onSettingConfirmed(gameName, gameDuration, masterNick)
                },
                backgroundColor = PixelYellow,
                shadowColor = PixelYellowShadow
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    CacciaAlTesoroSAMTheme {
        SettingScreen(
            modifier = Modifier,
            onBackClick = {},
            onSettingConfirmed = { _, _, _ -> }
        )
    }
}