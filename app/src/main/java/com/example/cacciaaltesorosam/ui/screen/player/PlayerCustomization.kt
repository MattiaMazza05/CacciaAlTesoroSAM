package com.example.cacciaaltesorosam.ui.screen.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.CacciaAlTesoroSAMTheme
import com.example.cacciaaltesorosam.ui.theme.PixelBlue
import com.example.cacciaaltesorosam.ui.theme.PixelGreen
import com.example.cacciaaltesorosam.ui.theme.PixelRed
import com.example.cacciaaltesorosam.ui.theme.PixelViolet
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow

@Composable
fun PlayerCustomization(
    modifier: Modifier,
    durata: Int,
    gameName: String,
    onStartclick: (String, Color) -> Unit
) {
    var playerName by remember { mutableStateOf("") }
    val coloriDisponibili = listOf(
        PixelRed,
        PixelGreen,
        PixelBlue,
        PixelYellow,
        PixelViolet
    )
    var coloreScelto by remember { mutableStateOf(coloriDisponibili.first()) }

    Column(modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${gameName}", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Spacer(modifier.height(4.dp))
        Text("Durata: $durata min")
        Spacer(modifier.height(32.dp))
        Text("IL TUO NOME")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = playerName,
            onValueChange = { playerName = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier.height(24.dp))
        Text("SELEZIONA UN COLORE")
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            coloriDisponibili.forEach { colore ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .background(colore)
                        .border(
                            width = if (colore == coloreScelto) 3.dp else 0.dp,
                            color = Color.White
                        )
                        .clickable { coloreScelto = colore }
                )
            }
        }
        Spacer(modifier.weight(1f))
        PixelButton(
            "SALVA E INIZIA",
            onClick = { onStartclick(playerName, coloreScelto) },
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}
