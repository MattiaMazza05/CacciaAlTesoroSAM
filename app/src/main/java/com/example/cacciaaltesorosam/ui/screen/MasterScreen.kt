package com.example.cacciaaltesorosam.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

enum class MasterScreens { InsertNick, RecordPoint }

@Composable
fun MasterScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    var currentMasterScreen by remember { mutableStateOf(MasterScreens.InsertNick) }
    when (currentMasterScreen) {
        MasterScreens.InsertNick -> InsertNick(modifier, onBackClick, onNameConfirmed = {
            currentMasterScreen =
                MasterScreens.RecordPoint
        })

        MasterScreens.RecordPoint -> MasterRecordScreen()
    }
}


@Composable
fun InsertNick(modifier: Modifier, onBackClick: () -> Unit, onNameConfirmed: (String) -> Unit) {
    var masterNickname by remember { mutableStateOf("") }

    Box(modifier.fillMaxSize()) {
        ElevatedButton(onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
            Text("Indietro")
        }
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text("Inserisci il nome del Master")
            }
            Row {
                TextField(
                    value = masterNickname,
                    onValueChange = { masterNickname = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
            Button({ onNameConfirmed(masterNickname) }, content = { Text("Avanti") })
        }
    }
}