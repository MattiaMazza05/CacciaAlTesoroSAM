package com.example.cacciaaltesorosam.ui.screen.master

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cacciaaltesorosam.R
import com.example.cacciaaltesorosam.data.StatoConnessione

@Composable
fun StatoPartitaAnimazione(stato: StatoConnessione) {
    val resId = when (stato) {
        StatoConnessione.RICEZIONE -> R.raw.master_ricezione
        StatoConnessione.ATTESA -> R.raw.master_attesa
        StatoConnessione.PRONTO -> R.raw.master_pronto
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(1f)
    )
}