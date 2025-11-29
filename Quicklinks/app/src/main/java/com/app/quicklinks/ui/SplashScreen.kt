package com.app.quicklinks.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.quicklinks.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    var animateStart by remember { mutableStateOf(false) }
    var showBlueLogo by remember { mutableStateOf(true) }

    val backgroundColor by animateColorAsState(
        targetValue = if (animateStart) Color.White else Color(0xFF4487E2),
        animationSpec = tween(durationMillis = 800),
        label = "background"
    )
    val logoTint by animateColorAsState(
        targetValue = if (animateStart) Color.Unspecified else Color.White,
        animationSpec = tween(800),
        label = "tint"
    )
    val scale by animateFloatAsState(
        targetValue = if (animateStart) 1f else 0.7f,
        animationSpec = tween(
            durationMillis = 800,
            easing = { OvershootInterpolator(2f).getInterpolation(it) }
        ),
        label = "scale"
    )

    LaunchedEffect(true) {
        delay(600)
        animateStart = true

        delay(900)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(250.dp)
                .scale(scale),
            colorFilter = if (!animateStart) {
                ColorFilter.tint(Color.White)
            } else null
        )
    }
}
class OvershootInterpolator(private val tension: Float = 2.0f) {
    fun getInterpolation(input: Float): Float {
        val t = input - 1.0f
        return t * t * ((tension + 1) * t + tension) + 1.0f
    }
}
