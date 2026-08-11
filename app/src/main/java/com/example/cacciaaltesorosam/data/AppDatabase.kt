package com.example.cacciaaltesorosam.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(entities = [GameEntity::class, LocationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null //variabile che tiene l'unica istanza esistente
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) { //ritorna l'istanza se esiste sennò crea db
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "caccia_al_tesoro_db"
                ).build()
                INSTANCE = instance
                instance //val di ritorno
            }
        }
    }
}
