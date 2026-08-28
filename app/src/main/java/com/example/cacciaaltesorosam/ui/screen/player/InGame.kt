package com.example.cacciaaltesorosam.ui.screen.player

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.cacciaaltesorosam.data.DistanzaStato
import com.example.cacciaaltesorosam.data.Game
import com.example.cacciaaltesorosam.data.PuntoCaccia
import com.example.cacciaaltesorosam.location.getPlayerLocation
import com.example.cacciaaltesorosam.ui.screen.common.GameTopBar
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.theme.PixelGreen
import com.example.cacciaaltesorosam.ui.theme.PixelGreenShadow
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow
import kotlinx.coroutines.delay


@Composable
fun InGame(modifier: Modifier, game: Game) {
    var tappaAttuale by remember { mutableStateOf(1) }
    var secondiRimanenti by remember { mutableStateOf(game.duration * 60) }
    var nextButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (secondiRimanenti > 0) {
            delay(1000)
            secondiRimanenti--
        }
    }
    val minuti = secondiRimanenti / 60
    val secondi = secondiRimanenti % 60
    val tempoRimanete = "%02d:%02d".format(minuti, secondi)
    val puntoCorrente = game.punti[tappaAttuale - 1]

    getPlayerLocation(LocalContext.current)

    val distanzaStato = calcolaDistanza(puntoCorrente)

    LaunchedEffect(distanzaStato) {
        if (distanzaStato == DistanzaStato.PUNTO_TROVATO || distanzaStato == DistanzaStato.TESORO_TROVATO) {
            nextButton = true
        }
    }

    Column(modifier.fillMaxSize()) {
        GameTopBar(
            tappaAttuale,
            game.punti.size,
            tempoRimanete,
            {}
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DistanzaAnimazione(distanzaStato)
            PixelButton(
                text = "RIPRODUCI INDIZIO",
                onClick = {},
                backgroundColor = PixelGreen,
                shadowColor = PixelGreenShadow
            )
        }
        PixelButton(
            "VAI AL PROSSIMO",
            enabled = nextButton,
            onClick = {
                if (tappaAttuale < game.punti.size) {
                    tappaAttuale++
                    nextButton = false
                }
            },
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun calcolaDistanza(puntoCorrente: PuntoCaccia): DistanzaStato {
    var distanzaStato by remember(puntoCorrente) { mutableStateOf(DistanzaStato.LONTANO) }
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasLocationPermission = isGranted }

    val actionVicino = "com.example.cacciaaltesorosam.PROXIMITY_VICINO"
    val actionTrovato = "com.example.cacciaaltesorosam.PROXIMITY_TROVATO"

    val receiver = remember(puntoCorrente) {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val dentroArea =
                    intent?.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false) ?: false
                if (dentroArea) {
                    when (intent?.action) {
                        actionVicino -> if (distanzaStato == DistanzaStato.LONTANO) distanzaStato =
                            DistanzaStato.VICINO

                        actionTrovato -> distanzaStato =
                            if (puntoCorrente.isTreasure) DistanzaStato.TESORO_TROVATO else DistanzaStato.PUNTO_TROVATO
                    }
                } else {
                    when (intent?.action) {
                        actionTrovato -> distanzaStato = DistanzaStato.VICINO
                        actionVicino -> distanzaStato = DistanzaStato.LONTANO
                    }
                }
            }
        }
    }

    val filter = remember {
        IntentFilter().apply {
            addAction(actionVicino)
            addAction(actionTrovato)
        }
    }

    DisposableEffect(puntoCorrente, hasLocationPermission) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return@DisposableEffect onDispose {}
        }

        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val intentVicino = Intent(actionVicino).apply { setPackage(context.packageName) }
        val piVicino = PendingIntent.getBroadcast(
            context,
            puntoCorrente.hashCode() + 1,
            intentVicino,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentTrovato = Intent(actionTrovato).apply { setPackage(context.packageName) }
        val piTrovato = PendingIntent.getBroadcast(
            context,
            puntoCorrente.hashCode() + 2,
            intentTrovato,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            lm.addProximityAlert(puntoCorrente.latitude, puntoCorrente.longitude, 10f, -1, piVicino)
            lm.addProximityAlert(puntoCorrente.latitude, puntoCorrente.longitude, 3f, -1, piTrovato)
        } catch (e: SecurityException) {

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }

        onDispose {
            try {
                lm.removeProximityAlert(piVicino)
                lm.removeProximityAlert(piTrovato)
                context.unregisterReceiver(receiver)
            } catch (e: Exception) {
            }
        }
    }

    return distanzaStato
}
