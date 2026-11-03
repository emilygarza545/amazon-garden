package com.Bibhavu3.Mamzon.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.di.AppViewModelFactory
import com.Bibhavu3.Mamzon.core.theme.BackgroundDeep
import com.Bibhavu3.Mamzon.core.theme.CanopyMint
import com.Bibhavu3.Mamzon.core.theme.DeepFoliage
import com.Bibhavu3.Mamzon.core.theme.RockGrey
import com.Bibhavu3.Mamzon.core.theme.SageText
import com.Bibhavu3.Mamzon.core.theme.ShadowDeep
import com.Bibhavu3.Mamzon.core.theme.SunlitGold
import com.Bibhavu3.Mamzon.core.theme.WarmCream
import com.Bibhavu3.Mamzon.core.ui.PrimaryButton
import com.Bibhavu3.Mamzon.core.ui.SecondaryButton
import com.Bibhavu3.Mamzon.core.ui.SeedBadge
import com.Bibhavu3.Mamzon.core.ui.StatCard
import com.Bibhavu3.Mamzon.core.ui.zoneTitle
import com.Bibhavu3.Mamzon.domain.model.Outcome
import com.Bibhavu3.Mamzon.domain.model.RoundResult

@Composable
fun GameOverScreen(
    result: RoundResult,
    onPlayAgain: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory())
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    GameOverContent(
        result = result,
        totalSeeds = state.totalSeeds,
        onPlayAgain = { onPlayAgain(result.nextLevelId) },
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    result: RoundResult,
    totalSeeds: Int,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val restored = result.outcome == Outcome.RESTORED
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val heroScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.7f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 320f),
        label = "gameOverHero"
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
                            BackgroundDeep.copy(alpha = 0.90f),
                            DeepFoliage.copy(alpha = 0.86f),
                            ShadowDeep.copy(alpha = 0.94f)
                        )
                    )
                )
        )
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = if (restored) {
                            listOf(SunlitGold.copy(alpha = 0.22f), Color.Transparent)
                        } else {
                            listOf(RockGrey.copy(alpha = 0.30f), Color.Transparent)
                        }
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SeedBadge(seeds = totalSeeds)
            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = painterResource(
                    if (restored) R.drawable.sprite_4 else R.drawable.sprite_2
                ),
                contentDescription = stringResource(
                    if (restored) R.string.cd_blue_morpho else R.string.cd_giant_fern
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(if (restored) 120.dp else 110.dp)
                    .scale(heroScale)
                    .alpha(if (restored) 1f else 0.6f)
            )
            Text(
                text = stringResource(zoneTitle(result.zone)),
                style = MaterialTheme.typography.labelSmall,
                color = CanopyMint,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(headlineFor(result.outcome)),
                style = MaterialTheme.typography.displaySmall,
                color = WarmCream,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            if (result.newBest) {
                Text(
                    text = stringResource(R.string.result_new_best),
                    style = MaterialTheme.typography.labelSmall,
                    color = SunlitGold,
                    textAlign = TextAlign.Center
                )
            }
            ResultStats(result = result)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(
                    if (restored) R.string.result_hint_win else R.string.result_hint_loss
                ),
                style = MaterialTheme.typography.labelSmall,
                color = SageText,
                textAlign = TextAlign.Center
            )
            PrimaryButton(
                text = stringResource(R.string.action_play_again),
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                text = stringResource(R.string.action_menu),
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ResultStats(result: RoundResult) {
    val cards = mutableListOf<ResultStat>()
    if (result.hydrated > 0) {
        cards.add(
            ResultStat(
                label = stringResource(R.string.stat_plants),
                value = stringResource(R.string.value_ratio, result.hydrated, result.required),
                accent = CanopyMint
            )
        )
    }
    if (result.movesUsed > 0) {
        cards.add(
            ResultStat(
                label = stringResource(R.string.stat_moves),
                value = stringResource(R.string.value_ratio, result.movesUsed, result.movesAllowed),
                accent = WarmCream
            )
        )
    }
    if (result.seedsEarned > 0) {
        cards.add(
            ResultStat(
                label = stringResource(R.string.stat_seeds),
                value = result.seedsEarned.toString(),
                accent = SunlitGold
            )
        )
    }
    if (cards.size >= 2) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            cards.forEach { card ->
                StatCard(
                    label = card.label,
                    value = card.value,
                    accent = card.accent,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private data class ResultStat(
    val label: String,
    val value: String,
    val accent: Color
)

private fun headlineFor(outcome: Outcome): Int = when (outcome) {
    Outcome.RESTORED -> R.string.result_restored
    Outcome.OUT_OF_MOVES -> R.string.result_out_of_moves
    Outcome.WITHERED -> R.string.result_withered
}
