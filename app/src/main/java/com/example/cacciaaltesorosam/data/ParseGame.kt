package com.example.cacciaaltesorosam.data

import org.json.JSONObject

fun ParseGame(json: String): Game {
    val obj = JSONObject(json)
    val puntiArray = obj.getJSONArray("points")
    val punti = mutableListOf<PuntoCaccia>()
    for (i in 0 until puntiArray.length()) {
        val p = puntiArray.getJSONObject(i)
        punti.add(
            PuntoCaccia(
                audioPath = p.getInt("audioIndex").toString(),
                isTreasure = p.getBoolean("isTreasure"),
                latitude = p.getDouble("latitude"),
                longitude = p.getDouble("longitude")
            )
        )
    }
    return Game(
        obj.getString("gameName"),
        obj.getInt("duration"),
        obj.getString("masterNick"),
        punti
    )
}