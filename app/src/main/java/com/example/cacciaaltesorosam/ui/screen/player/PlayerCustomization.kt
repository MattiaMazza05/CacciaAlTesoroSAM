package com.example.cacciaaltesorosam.ui.screen.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow

@Composable
fun PlayerCustomization(
    modifier: Modifier,
    durata: Int,
    gameName: String,
    onStartclick: () -> Unit
) {
    var playerName by remember { mutableStateOf("") }
    val coloriDisponibili = listOf(
        Color(0xFFE53935), // rosso
        Color(0xFF43A047), // verde
        Color(0xFF1E88E5), // blu
        Color(0xFFFDD835)  // giallo
    )
    var coloreScelto by remember { mutableStateOf(coloriDisponibili.first()) }

    Column(modifier.fillMaxSize()) {
        Text("NOME: ${gameName} DURATA: ${durata} min", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier.height(20.dp))
        Text("IL TUO NOME")
        OutlinedTextField(
            value = playerName,
            onValueChange = { playerName = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Text("SELEZIONA UN COLORE")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            coloriDisponibili.forEach { colore ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(colore)
                        .border(
                            width = if (colore == coloreScelto) 3.dp else 0.dp,
                            color = Color.White
                        )
                        .clickable { coloreScelto = colore }
                )
            }
        }
        Spacer(modifier.height(20.dp))
        PixelButton(
            "SALVA E INIZIA",
            onClick = { onStartclick() },
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow
        )
    }
}