package com.example.cacciaaltesorosam.ui.screen.player

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cacciaaltesorosam.data.Game

enum class PlayerScreens { DeviceList, PlayerCustomization, InGame, GameEnd }

@SuppressLint("MissingPermission")
@Composable
fun PlayerScreen(
    modifier: Modifier,
    onHomeClick: () -> Unit
) {
    var currentPlayerScreen by remember { mutableStateOf(PlayerScreens.DeviceList) }
    var giocoRicevuto by remember { mutableStateOf<Game?>(null) }
    var nomePlayer by remember { mutableStateOf("") }
    var colorePlayer by remember { mutableStateOf(Color.White) }
    var tempoTrascorso by remember { mutableStateOf(0) }
    var tesoroTrovato by remember { mutableStateOf(false) }
    when (currentPlayerScreen) {
        PlayerScreens.DeviceList -> DeviceList(modifier, onSelectClick = { gioco ->
            giocoRicevuto = gioco
            currentPlayerScreen =
                PlayerScreens.PlayerCustomization
        })

        PlayerScreens.PlayerCustomization -> {
            val gioco = giocoRicevuto
            if (gioco != null) {
                PlayerCustomization(
                    modifier,
                    durata = gioco.duration,
                    gioco.gameName,
                    onStartclick = { nome, colore ->
                        nomePlayer = nome
                        colorePlayer = colore
                        currentPlayerScreen =
                            PlayerScreens.InGame
                    })
            }
        }

        PlayerScreens.InGame -> {
            val gioco = giocoRicevuto
            if (gioco != null) {
                InGame(
                    modifier,
                    gioco,
                    onBackClick = { currentPlayerScreen = PlayerScreens.DeviceList },
                    onEndClick = { tempo, trovato ->
                        tempoTrascorso = tempo
                        currentPlayerScreen = PlayerScreens.GameEnd
                        tesoroTrovato = trovato
                    }
                )
            }
        }

        PlayerScreens.GameEnd -> {
            GameEnd(
                modifier,
                nomePlayer = nomePlayer,
                colorePlayer = colorePlayer,
                tempoTrascorso = tempoTrascorso,
                tesoroTrovato = tesoroTrovato,
                onHomeClick = onHomeClick
            )
        }
    }
}
