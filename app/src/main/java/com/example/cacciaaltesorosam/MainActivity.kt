package com.example.cacciaaltesorosam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.cacciaaltesorosam.ui.screen.MasterScreen
import com.example.cacciaaltesorosam.ui.screen.PlayerScreen
import com.example.cacciaaltesorosam.ui.theme.CacciaAlTesoroSAMTheme

enum class Screen { Hello, Master, Player }

val PixelFont = FontFamily(
    Font(R.font.press_start_2p)
)

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
                            onPlayerClick = { currentScreen = Screen.Player })

                        Screen.Master -> MasterScreen(
                            modifier = Modifier.padding(innerPadding),
                            onBackClick = { currentScreen = Screen.Hello })

                        Screen.Player -> PlayerScreen(modifier = Modifier.padding(innerPadding))
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
    onPlayerClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text("Benvenuto!", fontFamily = PixelFont)

        }
        Row { Text("Seleziona il tuo ruolo per iniziare", fontFamily = PixelFont) }
        Button(
            onClick = onPlayerClick
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(4.dp)
            ) {
                // Immagine sopra
                Image(
                    painter = painterResource(id = R.drawable.master),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp), // Leggermente più grande se sta sopra
                )
                // Testo sotto
                Text(
                    text = "Player",
                    fontFamily = PixelFont
                )
            }
        }
        Button(
            onClick = onMasterClick
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(4.dp)
            ) {
                // Immagine sopra
                Image(
                    painter = painterResource(id = R.drawable.master),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp), // Leggermente più grande se sta sopra
                )
                // Testo sotto
                Text(
                    text = "Master",
                    fontFamily = PixelFont
                )
            }
        }
    }
}