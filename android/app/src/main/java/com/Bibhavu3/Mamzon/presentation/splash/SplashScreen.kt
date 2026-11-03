package com.Bibhavu3.Mamzon.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.di.AppViewModelFactory
import com.Bibhavu3.Mamzon.core.theme.BackgroundDeep
import com.Bibhavu3.Mamzon.core.theme.DeepFoliage
import com.Bibhavu3.Mamzon.core.theme.JungleGreen
import com.Bibhavu3.Mamzon.core.theme.LeafMint
import com.Bibhavu3.Mamzon.core.theme.SageText
import com.Bibhavu3.Mamzon.core.theme.ShadowDeep
import com.Bibhavu3.Mamzon.core.theme.SunlitGold
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised
import com.Bibhavu3.Mamzon.core.theme.WarmCream

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory())
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    var handedOver by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(finished) {
        if (finished && !handedOver) {
            handedOver = true
            onFinished()
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    val transition = rememberInfiniteTransition(label = "splashLoops")
    val halo by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splashHalo"
    )
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "splashDrift"
    )
    val spin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "splashSpin"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            BackgroundDeep.copy(alpha = 0.94f),
                            DeepFoliage.copy(alpha = 0.90f),
                            ShadowDeep.copy(alpha = 0.97f)
                        )
                    )
                )
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val leafColor = LeafMint.copy(alpha = 0.14f)
            for (index in 0..2) {
                val phase = (drift + index * 0.33f) % 1f
                val x = size.width * (0.22f + 0.28f * index)
                val y = size.height * (1.08f - 1.25f * phase)
                rotate(degrees = spin + index * 120f, pivot = Offset(x, y)) {
                    drawOval(
                        color = leafColor,
                        topLeft = Offset(x - 30f, y - 52f),
                        size = Size(width = 60f, height = 104f)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Spacer(
                    modifier = Modifier
                        .size(260.dp)
                        .scale(halo)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    JungleGreen.copy(alpha = 0.38f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(168.dp)
                        .scale(halo)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displayMedium,
                color = WarmCream,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = LeafMint,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(44.dp))
            LinearProgressIndicator(
                color = SunlitGold,
                trackColor = SurfaceRaised,
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .width(180.dp)
                    .height(6.dp)
                    .background(SurfaceRaised, RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = SageText,
                textAlign = TextAlign.Center
            )
        }
    }
}
