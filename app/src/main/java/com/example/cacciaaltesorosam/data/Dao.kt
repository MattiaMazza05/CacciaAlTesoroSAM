package com.example.cacciaaltesorosam.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface Dao {
    @Insert
    suspend fun insertGame(game: GameEntity)

    @Insert
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * from caccia WHERE master_nick = :nick ORDER BY data DESC")
    suspend fun getGamesByMasterNick(nick: String): List<GameEntity>

    @Query("SELECT * FROM luogo WHERE game_id = :gameId ORDER BY id ASC")
    suspend fun getLocationsForGame(gameId: Int): List<LocationEntity>
}