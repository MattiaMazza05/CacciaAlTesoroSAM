package com.example.cacciaaltesorosam.ui.screen.master

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.cacciaaltesorosam.data.PuntoTemp

enum class MasterScreens { GameSettings, RecordPoint, Summary }

@Composable
fun MasterScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    var currentMasterScreen by remember { mutableStateOf(MasterScreens.GameSettings) }
    var punti by remember { mutableStateOf(listOf<PuntoTemp>()) }
    var gameName by remember { mutableStateOf("") }
    var gameDuration by remember { mutableStateOf(0) }
    when (currentMasterScreen) {
        MasterScreens.GameSettings -> SettingScreen(
            modifier,
            onBackClick,
            onSettingConfirmed = { nome, durata ->
                gameName = nome
                gameDuration = durata
                currentMasterScreen = MasterScreens.RecordPoint
            })

        MasterScreens.RecordPoint -> MasterRecordScreen(
            pointNumber = punti.size + 1,
            onPointConfirmed = { nuovoPunto ->
                punti = punti + nuovoPunto
                if (nuovoPunto.isTreasure) {
                    currentMasterScreen = MasterScreens.Summary
                    Log.d("MASTER", "Caccia completata con ${punti.size} punti")
                }
            })

        MasterScreens.Summary -> SummaryScreen(modifier, punti = punti)
    }
}
