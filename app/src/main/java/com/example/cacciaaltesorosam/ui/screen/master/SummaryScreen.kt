package com.example.cacciaaltesorosam.ui.screen.master

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.cacciaaltesorosam.data.AppDatabase
import com.example.cacciaaltesorosam.data.GameEntity
import com.example.cacciaaltesorosam.data.LocationEntity
import com.example.cacciaaltesorosam.data.PuntoCaccia
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelGreen
import com.example.cacciaaltesorosam.ui.theme.PixelGreenShadow
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SummaryScreen(
    modifier: Modifier,
    punti: List<PuntoCaccia>,
    gameName: String,
    duration: Int,
    masterNick: String,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        val context = LocalContext.current
        Text("CACCIA COMPLETA", style = MaterialTheme.typography.titleLarge)
        Text("${punti.size} tappe")

        LazyColumn {
            items(punti) { punto ->
                Column {
                    Text(if (punto.isTreasure) "TESORO" else "Tappa")
                    Text("Audio: ${punto.audioPath}")
                    Text("Lat: ${punto.latitude}, Lng: ${punto.longitude}")
                }
            }
        }
        Row {
            PixelButton(
                text = "SALVA E INIZIA",
                onClick = {
                    saveGame(context, punti, gameName, duration, masterNick)
                    onSendClick()
                },
                backgroundColor = PixelGreen,
                shadowColor = PixelGreenShadow,
            )
            PixelButton(
                text = "SALVA E TORNA ALLA HOME",
                onClick = {
                    saveGame(context, punti, gameName, duration, masterNick)
                    onBackClick()
                },
                backgroundColor = PixelPanel,
                shadowColor = PixelBorder,
                textColor = Color.White
            )
        }
    }
}

fun saveGame(
    context: Context,
    punti: List<PuntoCaccia>,
    gameName: String,
    duration: Int,
    masterNick: String
) {
    CoroutineScope(Dispatchers.IO).launch {
        val dao = AppDatabase.getDatabase(context).dao()
        val gameid = dao.insertGame(
            GameEntity(
                gameName = gameName,
                duration = duration,
                masterNick = masterNick
            )
        )
        for (punto in punti) {
            dao.insertLocation(
                LocationEntity(
                    audioTrack = punto.audioPath,
                    latitude = punto.latitude,
                    longitude = punto.longitude,
                    isTreasure = punto.isTreasure,
                    gameId = gameid
                )
            )
        }
        val giochiSalvati = dao.getAllGames()
        Log.d("DB_TEST", giochiSalvati.toString())
        val puntiSalvati = dao.getLocationsForGame(gameid)
        Log.d("DB_LOCATION_TEST", puntiSalvati.toString())
    }
}