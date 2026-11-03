package com.Bibhavu3.Mamzon.presentation.game

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.config.GameConfig
import com.Bibhavu3.Mamzon.core.di.AppViewModelFactory
import com.Bibhavu3.Mamzon.core.theme.BackgroundDeep
import com.Bibhavu3.Mamzon.core.theme.DeepFoliage
import com.Bibhavu3.Mamzon.core.theme.LeafMint
import com.Bibhavu3.Mamzon.core.theme.SageText
import com.Bibhavu3.Mamzon.core.theme.ShadowDeep
import com.Bibhavu3.Mamzon.core.theme.WarmCream
import com.Bibhavu3.Mamzon.core.ui.zoneTitle
import com.Bibhavu3.Mamzon.domain.model.RoundResult
import com.Bibhavu3.Mamzon.presentation.dialog.PauseDialog

@Composable
fun GameScreen(
    levelId: Int,
    attempt: Int,
    onFinished: (RoundResult) -> Unit,
    onExit: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(
        key = "game-$levelId-$attempt",
        factory = AppViewModelFactory(levelId = levelId)
    )
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { result ->
            onFinished(result)
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onTileTap = gameViewModel::onTileTap,
        onHint = gameViewModel::requestHint,
        onReset = gameViewModel::resetBoard,
        onPause = gameViewModel::pause,
        onExit = onExit
    )

    if (state.paused && state.phase != GamePhase.FINISHED) {
        PauseDialog(
            levelLabel = stringResource(R.string.game_level, state.levelId),
            onResume = gameViewModel::resume,
            onRestart = {
                gameViewModel.resetBoard()
                gameViewModel.resume()
            },
            onQuit = onExit
        )
    }
}

@Composable
private fun GameContent(
    state: GameUiState,
    onTileTap: (Int) -> Unit,
    onHint: () -> Unit,
    onReset: () -> Unit,
    onPause: () -> Unit,
    onExit: () -> Unit
) {
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
                            BackgroundDeep.copy(alpha = 0.86f),
                            DeepFoliage.copy(alpha = 0.78f),
                            ShadowDeep.copy(alpha = 0.92f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                zoneLabel = stringResource(zoneTitle(state.zone)),
                levelLabel = stringResource(R.string.game_level, state.levelId),
                onExit = onExit,
                onPause = onPause
            )
            MovesStrip(
                movesLeft = state.movesLeft,
                movesAllowed = state.movesAllowed,
                hydrated = state.hydrated.size,
                required = state.required
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                WaterwayBoard(
                    state = state,
                    onTileTap = onTileTap,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                FloatingControlBar(
                    hintEnabled = state.interactive &&
                        state.hintsEnabled &&
                        state.seeds >= GameConfig.HINT_COST,
                    onHint = onHint,
                    onReset = onReset,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun GameHeader(
    zoneLabel: String,
    levelLabel: String,
    onExit: () -> Unit,
    onPause: () -> Unit
) {
    val borderTint = LeafMint.copy(alpha = 0.18f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(DeepFoliage.copy(alpha = 0.72f))
            .drawBehind {
                drawLine(
                    color = borderTint,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f
                )
            }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onExit, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_back_to_garden),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = zoneLabel,
                style = MaterialTheme.typography.labelSmall,
                color = SageText,
                textAlign = TextAlign.Center
            )
            Text(
                text = levelLabel,
                style = MaterialTheme.typography.titleMedium,
                color = WarmCream,
                textAlign = TextAlign.Center
            )
        }
        IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
