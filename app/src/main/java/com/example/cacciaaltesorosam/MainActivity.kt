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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cacciaaltesorosam.ui.theme.CacciaAlTesoroSAMTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CacciaAlTesoroSAMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    hello(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun hello(modifier: Modifier){
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Row {
            Text("Benvenuto!")
            Text("Seleziona il tuo ruolo per iniziare")
        }
        Button(
            {},
            content = {Text("Master")}
        )
        Button(
            {},
            content = {Text("Host")}
        )
    }
}