package com.example.cacciaaltesorosam.ui.screen.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.cacciaaltesorosam.data.Coordinate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class UserLcoation(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var playerLocationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun getMasterLocation(
        onSuccess: (Coordinate) -> Unit,
        onError: (Exception?) -> Unit = {}
    ) {
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                onSuccess(Coordinate(location.latitude, location.longitude))
            } else {
                onError(Exception("Posizione non dsponibile"))
            }
        }.addOnFailureListener { exception ->
            onError(exception)
        }
    }

    fun stopPlayerTracking() {
        playerLocationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
            playerLocationCallback = null
        }
    }
}

@Composable
fun rememberLocationTracker(): UserLcoation {
    val context = LocalContext.current
    val tracker = remember(context) { UserLcoation(context) }

    DisposableEffect(tracker) {
        onDispose {
            tracker.stopPlayerTracking()
        }
    }
    return tracker
}