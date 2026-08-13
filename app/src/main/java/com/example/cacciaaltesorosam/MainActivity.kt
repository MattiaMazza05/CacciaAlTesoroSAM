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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.ui.screen.HistoricalScreen
import com.example.cacciaaltesorosam.ui.screen.MasterScreen
import com.example.cacciaaltesorosam.ui.screen.PixelButton
import com.example.cacciaaltesorosam.ui.screen.PlayerScreen
import com.example.cacciaaltesorosam.ui.theme.CacciaAlTesoroSAMTheme
import com.example.cacciaaltesorosam.ui.theme.PixelBorder
import com.example.cacciaaltesorosam.ui.theme.PixelPanel
import com.example.cacciaaltesorosam.ui.theme.PixelYellow
import com.example.cacciaaltesorosam.ui.theme.PixelYellowShadow

enum class Screen { Hello, Master, Player, Historical }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentScreen by remember { mutableStateOf(Screen.Hello) }
            CacciaAlTesoroSAMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (currentScreen) {
                        Screen.Hello -> hello(
                            modifier = Modifier.padding(innerPadding),
                            onMasterClick = { currentScreen = Screen.Master },
                            onPlayerClick = { currentScreen = Screen.Player },
                            onHistoricalClick = { currentScreen = Screen.Historical })

                        Screen.Master -> MasterScreen(
                            modifier = Modifier.padding(innerPadding),
                            onBackClick = { currentScreen = Screen.Hello })

                        Screen.Player -> PlayerScreen(modifier = Modifier.padding(innerPadding))
                        Screen.Historical -> HistoricalScreen(
                            modifier = Modifier.padding(
                                innerPadding
                            )
                        )
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
    onHistoricalClick: () -> Unit
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
    }
}