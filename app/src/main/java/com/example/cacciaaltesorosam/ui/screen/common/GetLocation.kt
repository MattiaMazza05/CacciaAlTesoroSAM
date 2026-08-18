package com.example.cacciaaltesorosam.ui.screen.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
                interval = TimeUnit.SECONDS.toMillis(60)
                fastestInterval = TimeUnit.SECONDS.toMillis(30)
                maxWaitTime = TimeUnit.MINUTES.toMillis(2)
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
        locProvider.removeLocationUpdates(locCallback)
    } catch (se: SecurityException) {
    }
}

@Composable
fun rememberMasterLocation(context: Context): MutableState<Coordinate?> {
    locProvider = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentLoc = remember {
        mutableStateOf<Coordinate?>(null)
    }
    DisposableEffect(key1 = locProvider) {
        locCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.locations.firstOrNull()
                if (loc != null) {
                    val ageMills = System.currentTimeMillis() - loc.time
                    if (ageMills < 10_000) {
                        currentLoc.value = Coordinate(loc.latitude, loc.longitude)
                        stopLocationUpdate()
                    }
                }
            }
        }
        onDispose { stopLocationUpdate() }
    }
    return currentLoc
}