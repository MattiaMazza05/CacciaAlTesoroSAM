package com.example.cacciaaltesorosam.ui.screen.player

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
import com.example.cacciaaltesorosam.data.DistanzaStato

@Composable
fun DistanzaAnimazione(stato: DistanzaStato) {
    val resId = when (stato) {
        DistanzaStato.LONTANO -> R.raw.player_lontano
        DistanzaStato.VICINO -> R.raw.player_vicino
        DistanzaStato.PUNTO_TROVATO -> R.raw.punto_trovato
        DistanzaStato.TESORO_TROVATO -> R.raw.tesoro_trovato

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