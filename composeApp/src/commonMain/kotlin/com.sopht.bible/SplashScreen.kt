package com.sopht.bible

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import styles.SophtBibleTheme

@Preview
@Composable
fun SplashScreen(onComplete: () -> Unit, modifier: Modifier = Modifier) {
    val animVisibleState = remember { MutableTransitionState(false) }
        .apply { targetState = true }
    if (animVisibleState.targetState == animVisibleState.currentState
    ) {
        onComplete()
    }

    SophtBibleTheme {
        AnimatedVisibility(
            visibleState = animVisibleState,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 5000)
            )+ slideIn (
                animationSpec = tween(durationMillis = 2000),
                initialOffset = { IntOffset(20, 20) }
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 2000)
            ) + shrinkOut (
                animationSpec = tween(durationMillis = 2000)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SB", modifier.align(Alignment.CenterHorizontally))
                Text("Sopht Bible", modifier.align(Alignment.CenterHorizontally))
                Text("Living & Powerful")
            }
        }
    }
}