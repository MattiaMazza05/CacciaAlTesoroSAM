package com.example.cacciaaltesorosam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cacciaaltesorosam.data.Game
import com.example.cacciaaltesorosam.data.PuntoCaccia
import com.example.cacciaaltesorosam.ui.screen.common.PixelButton
import com.example.cacciaaltesorosam.ui.screen.master.HistoricalScreen
import com.example.cacciaaltesorosam.ui.screen.master.MasterScreen
import com.example.cacciaaltesorosam.ui.screen.player.InGame
import com.example.cacciaaltesorosam.ui.screen.player.PlayerScreen
import com.example.cacciaaltesorosam.ui.theme.CacciaAlTesoroSAMTheme
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CacciaAlTesoroSAMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val demoGame = Game(
                        gameName = "Caccia di Prova",
                        duration = 1,
                        masterNick = "Mattia",
                        punti = listOf(
                            PuntoCaccia(
                                audioPath = "0",
                                isTreasure = false,
                                latitude = 43.7102122,
                                longitude = 10.3876207
                            ),
                            PuntoCaccia(
                                audioPath = "1",
                                isTreasure = false,
                                latitude = 43.7102122,
                                longitude = 10.3876207
                            ),
                            PuntoCaccia(
                                audioPath = "2",
                                isTreasure = true,
                                latitude = 43.7102122,
                                longitude = 10.3876207
                            )
                        )
                    )
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            hello(
                                modifier = Modifier.padding(innerPadding),
                                onMasterClick = { navController.navigate("master") },
                                onPlayerClick = { navController.navigate("player") },
                                onHistoricalClick = { navController.navigate("historical") },
                                onDemoClick = { navController.navigate("playerDemo") }
                            )
                        }
                        composable("master") {
                            MasterScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = { navController.popBackStack() })
                        }
                        composable("player") {
                            PlayerScreen(
                                modifier = Modifier.padding(innerPadding),
                                onHomeClick = { navController.navigate("home") })
                        }
                        composable("historical") {
                            HistoricalScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = { navController.navigate("home") }
                            )
                        }
                        composable("playerDemo") {
                            InGame(
                                modifier = Modifier.padding(innerPadding),
                                demoGame,
                                onBackClick = { navController.navigate("home") },
                                onEndClick = { _, _ -> navController.navigate("home") })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun hello(
    modifier: Modifier,
    onMasterClick: () -> Unit,
    onPlayerClick: () -> Unit,
    onHistoricalClick: () -> Unit,
    onDemoClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(
                "CACCIA AL TESORO",
                style = MaterialTheme.typography.titleLarge,
                color = PixelYellow
            )
        }
        Spacer(Modifier.heightIn(40.dp))
        PixelButton(
            text = "CREA PARTITA",
            onClick = onMasterClick,
            backgroundColor = PixelYellow,
            shadowColor = PixelYellowShadow
        )
        Spacer(Modifier.heightIn(14.dp))
        PixelButton(
            text = "LE MIE PARTITE",
            onClick = onHistoricalClick,
            backgroundColor = PixelPanel,
            textColor = Color.White,
            shadowColor = PixelBorder
        )
        Spacer(Modifier.height(14.dp))
        PixelButton(
            text = "UNISCITI A UNA PARTITA",
            onClick = onPlayerClick,
            backgroundColor = PixelPanel,
            textColor = Color.White,
            shadowColor = PixelBorder
        )
        Spacer(Modifier.height(14.dp))
        PixelButton(
            text = "PARTITA DEMO",
            onClick = onDemoClick,
            backgroundColor = PixelPanel,
            textColor = Color.White,
            shadowColor = PixelBorder
        )
    }
}