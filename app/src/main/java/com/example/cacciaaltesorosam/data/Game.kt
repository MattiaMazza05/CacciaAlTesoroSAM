package com.example.cacciaaltesorosam.data

data class Game(
    val gameName: String,
    val duration: Int,
    val masterNick: String,
    val punti: List<PuntoCaccia>
)
