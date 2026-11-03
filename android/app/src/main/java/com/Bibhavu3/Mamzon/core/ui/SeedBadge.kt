package com.Bibhavu3.Mamzon.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.theme.SunlitGold
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised

@Composable
fun SeedBadge(
    seeds: Int,
    modifier: Modifier = Modifier
) {
    val animatedSeeds by animateIntAsState(
        targetValue = seeds,
        animationSpec = tween(durationMillis = 600),
        label = "seedCounter"
    )
    val pulse by animateFloatAsState(
        targetValue = if (animatedSeeds == seeds) 1f else 1.08f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 420f),
        label = "seedPulse"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .height(44.dp)
            .widthIn(min = 112.dp)
            .scale(pulse)
            .background(
                color = SurfaceRaised.copy(alpha = 0.92f),
                shape = RoundedCornerShape(22.dp)
            )
            .border(
                width = 1.5.dp,
                color = SunlitGold.copy(alpha = 0.45f),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 14.dp)
            .semantics { liveRegion = LiveRegionMode.Polite }
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = stringResource(R.string.cd_seed_pouch),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = animatedSeeds.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = SunlitGold
        )
    }
}
