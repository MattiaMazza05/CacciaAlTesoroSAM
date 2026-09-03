package com.example.cacciaaltesorosam.ui.screen.master

import android.content.Context
import android.media.Image
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.R
import com.example.cacciaaltesorosam.data.AppDatabase
import com.example.cacciaaltesorosam.data.GameEntity
import com.example.cacciaaltesorosam.data.LocationEntity
import com.example.cacciaaltesorosam.data.PuntoCaccia
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.CacciaAlTesoroSAMTheme
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelGreen
import com.example.cacciaaltesorosam.ui.theme.PixelGreenShadow
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import com.example.cacciaaltesorosam.ui.theme.PixelViolet
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import androidx.compose.foundation.Image

@Composable
fun SummaryScreen(
    modifier: Modifier,
    punti: List<PuntoCaccia>,
    gameName: String,
    duration: Int,
    masterNick: String,
    onBackClick: () -> Unit,
    onSendClick: (String, List<String>) -> Unit
) {
    val context = LocalContext.current
    val jsonObject = JSONObject().apply {
        put("gameName", gameName)
        put("duration", duration)
        put("masterNick", masterNick)

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

    val gameJSON = jsonObject.toString()
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "CACCIA COMPLETA",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = PixelYellow
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("${punti.size} tappe", color = PixelGreen)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(punti) { index, punto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PixelPanel)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (punto.isTreasure) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_tesoro),
                            contentDescription = "Tesoro",
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Text(
                            "${index + 1}",
                            color = PixelGreen,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            if (punto.isTreasure) "TESORO" else "TAPPA ${index + 1}",
                            color = Color.White
                        )
                        Text(
                            "%.5f, %.5f".format(punto.latitude, punto.longitude),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.ic_audio),
                        contentDescription = "Indizio audio presente",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        PixelButton(
            text = "SALVA E INIZIA",
            onClick = {
                saveGame(context, punti, gameName, duration, masterNick)
                onSendClick(gameJSON, punti.map { it.audioPath })
            },
            backgroundColor = PixelGreen,
            shadowColor = PixelGreenShadow,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        PixelButton(
            text = "SALVA E TORNA ALLA HOME",
            onClick = {
                saveGame(context, punti, gameName, duration, masterNick)
                onBackClick()
            },
            backgroundColor = PixelPanel,
            shadowColor = PixelBorder,
            textColor = Color.White,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

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
                masterNick = masterNick,
                pointNumber = punti.size
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

@Preview(showBackground = true)
@Composable
fun SummaryScreenPreview() {
    CacciaAlTesoroSAMTheme {
        SummaryScreen(
            modifier = Modifier,
            punti = listOf(
                PuntoCaccia("path1", false, 43.71021, 10.38762),
                PuntoCaccia("path2", false, 43.71105, 10.38820),
                PuntoCaccia("path3", true, 43.71200, 10.38900)
            ),
            gameName = "Caccia di Prova",
            duration = 30,
            masterNick = "Mattia",
            onBackClick = {},
            onSendClick = { _, _ -> }
        )
    }
}