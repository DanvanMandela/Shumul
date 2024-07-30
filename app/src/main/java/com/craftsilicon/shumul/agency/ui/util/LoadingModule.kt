package com.craftsilicon.shumul.agency.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.craftsilicon.shumul.agency.R
import com.valentinilk.shimmer.shimmer

@Composable
fun LoadingModule() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hour_glass))
    Box(modifier = Modifier.fillMaxSize()) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .padding(24.dp)
                .size(60.dp)
                .align(Alignment.Center)
        )
    }
}


@Composable
fun LoadingModuleMain() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.shumul_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(24.dp)
                .size(60.dp)
                .align(Alignment.Center)
                .shimmer()
        )
    }
}