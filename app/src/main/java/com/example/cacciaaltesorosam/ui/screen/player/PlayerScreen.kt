package com.example.cacciaaltesorosam.ui.screen.player

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.cacciaaltesorosam.data.Game

enum class PlayerScreens { DeviceList, PlayerCustomization, InGame }

@SuppressLint("MissingPermission")
@Composable
fun PlayerScreen(
    modifier: Modifier
) {
    var currentPlayerScreen by remember { mutableStateOf(PlayerScreens.DeviceList) }
    var giocoRicevuto by remember { mutableStateOf<Game?>(null) }
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
                    onStartclick = {
                        currentPlayerScreen =
                            PlayerScreens.InGame
                    })
            }
        }

        PlayerScreens.InGame -> {
            val gioco = giocoRicevuto
            if (gioco != null) {
                InGame(modifier, gioco)
            }
        }
    }
}
