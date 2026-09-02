package com.example.cacciaaltesorosam.ui.screen.master

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cacciaaltesorosam.data.StatoConnessione

@Composable
fun PlayerInteraction(modifier: Modifier, stato: StatoConnessione) {
    Column(modifier.fillMaxSize()) {
        Row {
            when (stato) {
                StatoConnessione.PRONTO -> Text("INVIANDO LA PARTITA")
                StatoConnessione.ATTESA -> Text("ATTENDI CHE I PLAYER GIOCHINO")
                StatoConnessione.RICEZIONE -> Text("OTTENENDO RISULTATI")
            }
        }
        StatoPartitaAnimazione(stato)
    }
}