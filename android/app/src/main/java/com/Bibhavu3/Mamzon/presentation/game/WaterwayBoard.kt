package com.Bibhavu3.Mamzon.presentation.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.config.GameConfig
import com.Bibhavu3.Mamzon.core.theme.LeafMint
import com.Bibhavu3.Mamzon.core.theme.ShadowDeep
import com.Bibhavu3.Mamzon.core.theme.SoilTile
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised
import com.Bibhavu3.Mamzon.core.theme.WarmCream
import com.Bibhavu3.Mamzon.domain.model.TileKind

@Composable
fun WaterwayBoard(
    state: GameUiState,
    onTileTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dryLabel = stringResource(R.string.state_tile_dry)
    val wateredLabel = stringResource(R.string.state_tile_watered)
    val floodedLabel = stringResource(R.string.state_tile_flooded)
    val lockedLabel = stringResource(R.string.state_tile_locked)

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        val frame = GameConfig.BOARD_PAD_DP.dp + GameConfig.BOARD_BORDER_DP.dp
        val boardMax = min(maxWidth - 32.dp, GameConfig.BOARD_MAX_DP.dp)
        val tileSize = ((boardMax - frame * 2).value / GameConfig.BOARD_COLS).toInt().dp
        val boardWidth = tileSize * GameConfig.BOARD_COLS + frame * 2
        val boardHeight = tileSize * GameConfig.BOARD_ROWS + frame * 2
        val shape = RoundedCornerShape(28.dp)

        Column(
            modifier = Modifier
                .size(width = boardWidth, height = boardHeight)
                .shadow(
                    elevation = 18.dp,
                    shape = shape,
                    ambientColor = ShadowDeep,
                    spotColor = ShadowDeep
                )
                .background(
                    brush = Brush.verticalGradient(listOf(SoilTile, SurfaceRaised)),
                    shape = shape
                )
                .border(
                    width = GameConfig.BOARD_BORDER_DP.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            WarmCream.copy(alpha = 0.10f),
                            LeafMint.copy(alpha = 0.30f),
                            Color.Transparent
                        )
                    ),
                    shape = shape
                )
                .padding(GameConfig.BOARD_PAD_DP.dp)
        ) {
            for (row in 0 until GameConfig.BOARD_ROWS) {
                Row {
                    for (col in 0 until GameConfig.BOARD_COLS) {
                        val index = row * GameConfig.BOARD_COLS + col
                        val tile = state.tiles.getOrNull(index)
                        if (tile == null) {
                            Spacer(modifier = Modifier.size(tileSize))
                        } else {
                            val drowned = index in state.drowned
                            val hydrated = index in state.hydrated
                            val wet = index in state.wet
                            WaterTile(
                                tile = tile,
                                size = tileSize,
                                wet = wet,
                                distance = state.distance[index] ?: 0,
                                hydrated = hydrated,
                                drowned = drowned,
                                hinted = state.hintIndex == index,
                                interactive = state.interactive,
                                stateLabel = when {
                                    tile.kind == TileKind.PLANT && drowned -> floodedLabel
                                    tile.kind == TileKind.PLANT && hydrated -> wateredLabel
                                    tile.kind == TileKind.PLANT -> dryLabel
                                    !tile.rotatable -> lockedLabel
                                    wet -> wateredLabel
                                    else -> dryLabel
                                },
                                onTap = onTileTap
                            )
                        }
                    }
                }
            }
        }
    }
}
