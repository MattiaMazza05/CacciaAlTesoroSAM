package com.example.cacciaaltesorosam.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.cacciaaltesorosam.R
import com.example.cacciaaltesorosam.data.PuntoTemp

enum class MasterScreens { InsertNick, RecordPoint }

@Composable
fun MasterScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    var currentMasterScreen by remember { mutableStateOf(MasterScreens.InsertNick) }
    var punti by remember { mutableStateOf(listOf<PuntoTemp>()) }
    when (currentMasterScreen) {
        MasterScreens.InsertNick -> InsertNick(modifier, onBackClick, onNameConfirmed = {
            currentMasterScreen =
                MasterScreens.RecordPoint
        })

        MasterScreens.RecordPoint -> MasterRecordScreen(onPointConfirmed = { nuovoPunto ->
            punti = punti + nuovoPunto
            if (nuovoPunto.isTreasure) {
                Log.d("MASTER", "Caccia completata con ${punti.size} punti")
            }
        })
    }
}

@Composable
fun InsertNick(modifier: Modifier, onBackClick: () -> Unit, onNameConfirmed: (String) -> Unit) {
    var masterNickname by remember { mutableStateOf("") }

    Box(modifier.fillMaxSize()) {
        IconButton(
            onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = "Torna indietro"
            )
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
                OutlinedTextField(
                    value = masterNickname,
                    onValueChange = { masterNickname = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
            Button({ onNameConfirmed(masterNickname) }, content = { Text("Avanti") })
        }
    }
}