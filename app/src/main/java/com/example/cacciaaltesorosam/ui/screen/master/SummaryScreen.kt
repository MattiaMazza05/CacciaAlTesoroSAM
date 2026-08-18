package com.example.cacciaaltesorosam.ui.screen.master

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cacciaaltesorosam.data.PuntoTemp

@Composable
fun SummaryScreen(modifier: Modifier, punti: List<PuntoTemp>) {
    Column(modifier = modifier.fillMaxSize()) {
        Text("CACCIA COMPLETA", style = MaterialTheme.typography.titleLarge)
        Text("${punti.size} tappe")

        LazyColumn {
            items(punti) { punto ->
                Column {
                    Text(if (punto.isTreasure) "TESORO" else "Tappa")
                    Text("Audio: ${punto.audioPath}")
                    Text("Lat: ${punto.latitude}, Lng: ${punto.longitude}")
                }
            }
        }
    }
}