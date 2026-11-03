package com.Bibhavu3.Mamzon.presentation.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Bibhavu3.Mamzon.core.config.GameConfig
import com.Bibhavu3.Mamzon.core.theme.CanopyMint
import com.Bibhavu3.Mamzon.core.theme.LeafMint
import com.Bibhavu3.Mamzon.core.theme.MutedText
import com.Bibhavu3.Mamzon.core.theme.RockGrey
import com.Bibhavu3.Mamzon.core.theme.ShadowDeep
import com.Bibhavu3.Mamzon.core.theme.SoilDry
import com.Bibhavu3.Mamzon.core.theme.SoilWet
import com.Bibhavu3.Mamzon.core.theme.SunlitGold
import com.Bibhavu3.Mamzon.core.ui.speciesArt
import com.Bibhavu3.Mamzon.domain.model.Edge
import com.Bibhavu3.Mamzon.domain.model.FlowLayer
import com.Bibhavu3.Mamzon.domain.model.Tile
import com.Bibhavu3.Mamzon.domain.model.TileKind

@Composable
fun WaterTile(
    tile: Tile,
    size: Dp,
    wet: Boolean,
    distance: Int,
    hydrated: Boolean,
    drowned: Boolean,
    hinted: Boolean,
    interactive: Boolean,
    stateLabel: String,
    onTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val soil by animateColorAsState(
        targetValue = if (wet) SoilWet else SoilDry,
        animationSpec = tween(durationMillis = 260),
        label = "tileSoil"
    )
    val spin by animateFloatAsState(
        targetValue = tile.rotation * 90f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 420f),
        label = "tileSpin"
    )
    val waterAlpha by animateFloatAsState(
        targetValue = if (wet) 1f else 0.26f,
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = if (wet) distance * GameConfig.FLOW_STEP_MS else 0
        ),
        label = "tileWater"
    )
    val bloom by animateFloatAsState(
        targetValue = if (hydrated) 1.12f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 360f),
        label = "tileBloom"
    )

    Box(
        modifier = modifier
            .size(size)
            .padding(3.dp)
            .background(color = soil, shape = RoundedCornerShape(14.dp))
            .border(
                width = if (hinted) 2.dp else 1.dp,
                color = if (hinted) SunlitGold else ShadowDeep.copy(alpha = 0.35f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(enabled = interactive && tile.rotatable) { onTap(tile.index) }
            .semantics { stateDescription = stateLabel },
        contentAlignment = Alignment.Center
    ) {
        when (tile.kind) {
            TileKind.CHANNEL, TileKind.RISER, TileKind.SPRING ->
                ChannelArt(tile = tile, spin = spin, waterAlpha = waterAlpha)
            TileKind.PLANT ->
                PlantArt(tile = tile, bloom = bloom, drowned = drowned)
            TileKind.ROCK ->
                Spacer(
                    modifier = Modifier
                        .size(size * 0.46f)
                        .background(color = RockGrey, shape = RoundedCornerShape(12.dp))
                )
            TileKind.SOIL ->
                Spacer(modifier = Modifier.size(1.dp))
        }
    }
}

@Composable
private fun ChannelArt(tile: Tile, spin: Float, waterAlpha: Float) {
    val canopy = tile.layer == FlowLayer.CANOPY
    val stroke = if (canopy) CanopyMint else LeafMint
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .rotate(spin)
    ) {
        val centre = Offset(size.width / 2f, size.height / 2f)
        val width = size.minDimension * 0.22f
        val lift = if (canopy) size.minDimension * 0.08f else 0f
        if (canopy) {
            tile.connections.forEach { edge ->
                drawLine(
                    color = ShadowDeep.copy(alpha = 0.45f),
                    start = centre,
                    end = edgePoint(edge, size.width, size.height),
                    strokeWidth = width,
                    cap = StrokeCap.Round
                )
            }
        }
        tile.connections.forEach { edge ->
            val target = edgePoint(edge, size.width, size.height)
            drawLine(
                color = stroke.copy(alpha = waterAlpha),
                start = Offset(centre.x, centre.y - lift),
                end = Offset(target.x, target.y - lift),
                strokeWidth = width,
                cap = StrokeCap.Round
            )
        }
        if (tile.kind == TileKind.SPRING) {
            drawCircle(
                color = SunlitGold,
                radius = size.minDimension * 0.19f,
                center = centre,
                style = Stroke(width = width * 0.6f)
            )
            drawCircle(
                color = CanopyMint.copy(alpha = 0.5f),
                radius = size.minDimension * 0.1f,
                center = centre
            )
        }
        if (tile.kind == TileKind.RISER) {
            drawCircle(
                color = SunlitGold.copy(alpha = 0.9f),
                radius = size.minDimension * 0.14f,
                center = centre,
                style = Stroke(width = width * 0.5f)
            )
        }
    }
}

@Composable
private fun PlantArt(tile: Tile, bloom: Float, drowned: Boolean) {
    val art = tile.species?.let { speciesArt(it) }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (art != null) {
            Image(
                painter = painterResource(art.image),
                contentDescription = stringResource(art.title),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 12.dp)
                    .scale(if (drowned) 0.92f else bloom)
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            repeat(tile.thirst) {
                Spacer(
                    modifier = Modifier
                        .size(5.dp)
                        .background(
                            color = if (drowned) MutedText else CanopyMint,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

private fun edgePoint(edge: Edge, width: Float, height: Float): Offset = when (edge) {
    Edge.TOP -> Offset(width / 2f, 0f)
    Edge.RIGHT -> Offset(width, height / 2f)
    Edge.BOTTOM -> Offset(width / 2f, height)
    Edge.LEFT -> Offset(0f, height / 2f)
}
