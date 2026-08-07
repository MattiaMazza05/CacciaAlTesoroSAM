package com.example.cacciaaltesorosam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.cacciaaltesorosam.ui.screen.MasterScreen
import com.example.cacciaaltesorosam.ui.screen.PlayerScreen
import com.example.cacciaaltesorosam.ui.theme.CacciaAlTesoroSAMTheme

enum class Screen { Hello, Master, Player }
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

                        Screen.Master -> MasterScreen(modifier = Modifier.padding(innerPadding),  onBackClick = {currentScreen = Screen.Hello})
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
            Text("Benvenuto!")
        }
        Row { Text("Seleziona il tuo ruolo per iniziare") }
        Button(
            onClick = onMasterClick,
            content = { Text("Master") }
        )
        Button(
            onClick = onPlayerClick,
            content = { Text("Player") }
        )
    }
}