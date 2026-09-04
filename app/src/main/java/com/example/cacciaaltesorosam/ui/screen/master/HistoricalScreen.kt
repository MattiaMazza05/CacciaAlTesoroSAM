package com.example.cacciaaltesorosam.ui.screen.master

import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.data.AppDatabase
import com.example.cacciaaltesorosam.data.GameEntity
import com.example.cacciaaltesorosam.data.RisultatoGame
import com.example.cacciaaltesorosam.data.StatoConnessione
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.common.PixelTopBar
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

enum class HistoricalScreen { Lista, SendGame, PlayerInteraction }

@Composable
fun HistoricalScreen(
    modifier: Modifier,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    var archivio by remember { mutableStateOf(listOf<GameEntity>()) }
    var gameJSON by remember { mutableStateOf<String?>(null) }
    var selectedMasterNick by remember { mutableStateOf("") }
    var audioPaths by remember { mutableStateOf(listOf<String>()) }
    var currentScreen by remember { mutableStateOf(HistoricalScreen.Lista) }
    var statoConnessione by remember { mutableStateOf(StatoConnessione.ATTESA) }
    var risultati by remember { mutableStateOf(listOf<RisultatoGame>()) }
    val bta = BluetoothAdapter.getDefaultAdapter()

    LaunchedEffect(Unit) {
        val dao = AppDatabase.getDatabase(context).dao()
        archivio = dao.getAllGames()
    }

    when (currentScreen) {
        HistoricalScreen.Lista -> Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PixelTopBar(title = "ARCHIVIO", onBackClick = onBackClick)
            Spacer(modifier = Modifier.height(16.dp))
            if (archivio.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Nessuna caccia salvata ancora",
                        textAlign = TextAlign.Center
                    )
                }
            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(archivio) { game ->

                        PixelButton(
                            text = "Nome: ${game.gameName} Durata: ${game.duration} min Punti: ${game.pointNumber}",
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val dao = AppDatabase.getDatabase(context).dao()
                                    val punti = dao.getLocationsForGame(game.id)
                                    audioPaths = punti.map { it.audioTrack }

                                    val jsonObject = JSONObject().apply {
                                        put("gameName", game.gameName)
                                        put("duration", game.duration)
                                        put("masterNick", game.masterNick)

                                        val puntiArray = JSONArray()
                                        punti.forEachIndexed { index, punto ->
                                            val puntoJson = JSONObject().apply {
                                                put("audioIndex", index)
                                                put("isTreasure", punto.isTreasure)
                                                put("latitude", punto.latitude)
                                                put("longitude", punto.longitude)
                                            }
                                            puntiArray.put(puntoJson)
                                        }
                                        put("points", puntiArray)

                                    }
                                    selectedMasterNick = game.masterNick
                                    gameJSON = jsonObject.toString()
                                    currentScreen = HistoricalScreen.SendGame

                                }
                            },
                            backgroundColor = PixelPanel,
                            shadowColor = PixelBorder,
                            textColor = Color.White
                        )

                    }
                }
            }

        }

        HistoricalScreen.PlayerInteraction -> PlayerInteraction(
            modifier,
            statoConnessione,
            risultati = risultati,
            onBackClick = onBackClick,
            onRiceviClick = {
                statoConnessione =
                    StatoConnessione.RICEZIONE
                pairPlayer(bta = bta) { risultatoGame ->
                    risultati = risultati + risultatoGame
                }
            })

        HistoricalScreen.SendGame -> SendGammeViaBluetooth(
            modifier,
            selectedMasterNick,
            gameJSON,
            audioPaths,
            onWaitClick = { currentScreen = HistoricalScreen.PlayerInteraction },
            onStatoChange = { stato -> statoConnessione = stato })
    }

}