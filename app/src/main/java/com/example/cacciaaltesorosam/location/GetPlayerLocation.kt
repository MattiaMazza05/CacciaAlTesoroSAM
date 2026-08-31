package com.example.cacciaaltesorosam.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cacciaaltesorosam.data.Coordinate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

lateinit var locCallback: LocationCallback
lateinit var locProvider: FusedLocationProviderClient

@SuppressLint("MissingPermission")
fun locationUpdate() {
    locCallback.let {
        val locRequest: LocationRequest =
            LocationRequest.create().apply {
                interval = TimeUnit.SECONDS.toMillis(3)
                fastestInterval = TimeUnit.SECONDS.toMillis(1)
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        locProvider.requestLocationUpdates(
            locRequest,
            it,
            Looper.getMainLooper()
        )
    }
}

fun stopLocationUpdate() {
    try {
        val removetask = locProvider.removeLocationUpdates(locCallback)
        removetask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("LOCATION", "Aggiornamenti posizione rimossi correttamente")
            } else {
                Log.e("LOCATION", "Rimozione aggiornamenti posizione fallita")
            }
        }
    } catch (se: SecurityException) {
        Log.e("LOCATION", "Permesso mancante per rimuovere gli aggiornamenti", se)
    }
}

@SuppressLint("MissingPermission")
@Composable
fun getPlayerLocation(context: Context): Coordinate {
    locProvider = LocationServices.getFusedLocationProviderClient(context)
    var currentLoc by remember { mutableStateOf(Coordinate(0.0, 0.0)) }
    DisposableEffect(Unit) {
        locCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                for (loc in result.locations) {
                    currentLoc = Coordinate(loc.latitude, loc.longitude)
                    Log.d(
                        "LOCATION_DEBUG",
                        "Rilevato: ${loc.latitude}, ${loc.longitude} (accuratezza: ${loc.accuracy}m)"
                    )
                }
            }
        }
        locationUpdate()
        onDispose { stopLocationUpdate() }
    }
    return currentLoc
}