package com.example.cacciaaltesorosam.data

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey

@Entity(tableName = "caccia")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "nome_caccia") val gameName: String,
    @ColumnInfo(name = "data") val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "durata") val duration: Int,
    @ColumnInfo(name = "nome_master") val masterNick: String,
    @ColumnInfo(name = "num_punti") val pointNumber: Int
)


@Entity(
    tableName = "luogo",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["game_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "traccia_audio") val audioTrack: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "tesoro") val isTreasure: Boolean,
    @ColumnInfo(name = "game_id") val gameId: Long
)