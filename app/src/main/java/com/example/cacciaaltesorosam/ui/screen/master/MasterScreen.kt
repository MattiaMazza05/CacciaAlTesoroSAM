package com.example.cacciaaltesorosam.ui.screen.master

import android.bluetooth.BluetoothAdapter
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.cacciaaltesorosam.data.PuntoCaccia
import com.example.cacciaaltesorosam.data.RisultatoGame
import com.example.cacciaaltesorosam.data.StatoConnessione

enum class MasterScreens { GameSettings, RecordPoint, Summary, SendGame, PlayerInteraction }

@Composable
fun MasterScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    var currentMasterScreen by remember { mutableStateOf(MasterScreens.GameSettings) }
    var punti by remember { mutableStateOf(listOf<PuntoCaccia>()) }
    var gameName by remember { mutableStateOf("") }
    var gameDuration by remember { mutableStateOf(0) }
    var masterNick by remember { mutableStateOf("") }
    var gameJSON by remember { mutableStateOf("") }
    var audioPaths by remember { mutableStateOf(listOf<String>()) }
    var statoConnessione by remember { mutableStateOf(StatoConnessione.ATTESA) }
    val bta = BluetoothAdapter.getDefaultAdapter()
    var risultati by remember { mutableStateOf(listOf<RisultatoGame>()) }
    when (currentMasterScreen) {
        MasterScreens.GameSettings -> SettingScreen(
            modifier,
            onBackClick,
            onSettingConfirmed = { nome, durata, nomeMaster ->
                gameDuration = durata
                gameName = nome
                masterNick = nomeMaster
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

            },
            onMasterBackClick = {
                currentMasterScreen = MasterScreens.GameSettings
            },

            )

        MasterScreens.Summary -> SummaryScreen(
            modifier, punti = punti, gameName = gameName,
            duration = gameDuration,
            masterNick = masterNick,
            onBackClick,
            onSendClick = { string, paths ->
                gameJSON = string
                audioPaths = paths
                currentMasterScreen = MasterScreens.SendGame
            }
        )

        MasterScreens.SendGame -> SendGammeViaBluetooth(
            modifier,
            masterNick,
            gameJSON,
            audioPaths,
            onWaitClick = {
                currentMasterScreen =
                    MasterScreens.PlayerInteraction
            },
            onStatoChange = { stato -> statoConnessione = stato })

        MasterScreens.PlayerInteraction -> PlayerInteraction(
            modifier,
            statoConnessione,
            risultati = risultati,
            onBackClick = onBackClick,
            onRiceviClick = {
                statoConnessione =
                    StatoConnessione.RICEZIONE
                pairPlayer(bta) { risultatoGame ->
                    Log.d("RISULTATI_MASTER", "Ricevuto: $risultatoGame")
                    risultati = risultati + risultatoGame
                }
            })
    }
}
