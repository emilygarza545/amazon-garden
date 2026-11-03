package com.Bibhavu3.Mamzon.presentation.menu

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.di.AppViewModelFactory
import com.Bibhavu3.Mamzon.core.theme.DeepFoliage
import com.Bibhavu3.Mamzon.core.theme.LeafMint
import com.Bibhavu3.Mamzon.core.theme.SageText
import com.Bibhavu3.Mamzon.core.theme.SunlitGold
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised
import com.Bibhavu3.Mamzon.core.theme.WarmCream
import com.Bibhavu3.Mamzon.core.ui.ClayPanel
import com.Bibhavu3.Mamzon.core.ui.PrimaryButton
import com.Bibhavu3.Mamzon.core.ui.SecondaryButton
import com.Bibhavu3.Mamzon.core.ui.SeedBadge
import com.Bibhavu3.Mamzon.core.ui.StatCard
import com.Bibhavu3.Mamzon.core.ui.ZoneChip
import com.Bibhavu3.Mamzon.core.ui.zoneTitle
import com.Bibhavu3.Mamzon.domain.model.ZoneId
import com.Bibhavu3.Mamzon.presentation.dialog.GardenAtlasDialog
import com.Bibhavu3.Mamzon.presentation.dialog.SettingsDialog

@Composable
fun MenuScreen(onPlay: (Int) -> Unit) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory())
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    MenuContent(
        state = state,
        onPlay = { onPlay(state.nextLevelId) },
        onSelectZone = menuViewModel::selectZone,
        onOpenAtlas = menuViewModel::openAtlas,
        onOpenSettings = menuViewModel::openSettings
    )

    if (state.atlasVisible) {
        GardenAtlasDialog(
            species = state.atlasSpecies,
            discovered = state.discoveredSpecies,
            decorations = state.decorations,
            owned = state.ownedDecorations,
            equipped = state.equippedDecoration,
            totalSeeds = state.totalSeeds,
            onBuy = menuViewModel::buyDecoration,
            onEquip = menuViewModel::equipDecoration,
            onDismiss = menuViewModel::closeAtlas
        )
    }

    if (state.settingsVisible) {
        SettingsDialog(
            soundEnabled = state.soundEnabled,
            hapticsEnabled = state.hapticsEnabled,
            hintsEnabled = state.hintsEnabled,
            resetConfirmVisible = state.resetConfirmVisible,
            onSoundChange = menuViewModel::setSoundEnabled,
            onHapticsChange = menuViewModel::setHapticsEnabled,
            onHintsChange = menuViewModel::setHintsEnabled,
            onAskReset = menuViewModel::askResetConfirm,
            onCancelReset = menuViewModel::cancelResetConfirm,
            onConfirmReset = menuViewModel::confirmReset,
            onDismiss = menuViewModel::closeSettings
        )
    }
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onSelectZone: (ZoneId) -> Unit,
    onOpenAtlas: () -> Unit,
    onOpenSettings: () -> Unit
) {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        revealed = true
    }
    val sheetOffset by animateFloatAsState(
        targetValue = if (revealed) 0f else 28f,
        animationSpec = tween(durationMillis = 380),
        label = "menuSheetOffset"
    )
    val sheetAlpha by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(durationMillis = 380),
        label = "menuSheetAlpha"
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
                            DeepFoliage.copy(alpha = 0.26f),
                            DeepFoliage.copy(alpha = 0.52f),
                            DeepFoliage.copy(alpha = 0.88f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ArtBand(
                seeds = state.totalSeeds,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(min = 180.dp)
            )
            ClayPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = sheetOffset.dp)
                    .alpha(sheetAlpha),
                shape = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp),
                elevation = 22.dp,
                topColor = SurfaceRaised,
                bottomColor = DeepFoliage
            ) {
                MenuSheet(
                    state = state,
                    onPlay = onPlay,
                    onSelectZone = onSelectZone,
                    onOpenAtlas = onOpenAtlas,
                    onOpenSettings = onOpenSettings
                )
            }
        }
    }
}

@Composable
private fun ArtBand(seeds: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            DeepFoliage.copy(alpha = 0.55f),
                            DeepFoliage.copy(alpha = 0.95f)
                        )
                    )
                )
        )
        SeedBadge(
            seeds = seeds,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 12.dp)
        )
        Image(
            painter = painterResource(R.drawable.sprite_4),
            contentDescription = stringResource(R.string.cd_blue_morpho),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = 18.dp)
                .size(64.dp)
        )
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .size(118.dp)
        )
    }
}

@Composable
private fun MenuSheet(
    state: MenuUiState,
    onPlay: () -> Unit,
    onSelectZone: (ZoneId) -> Unit,
    onOpenAtlas: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 18.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.menu_title),
            style = MaterialTheme.typography.headlineMedium,
            color = WarmCream,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.menu_tagline),
            style = MaterialTheme.typography.labelSmall,
            color = SageText,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.zoneOrder.forEach { zone ->
                val locked = zone !in state.unlockedZones
                ZoneChip(
                    label = stringResource(zoneChipLabel(zone)),
                    selected = zone == state.selectedZone,
                    locked = locked,
                    stateLabel = when {
                        locked -> stringResource(R.string.state_locked)
                        zone == state.selectedZone -> stringResource(R.string.state_selected)
                        else -> stringResource(R.string.state_available)
                    },
                    onClick = { onSelectZone(zone) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        if (state.statsVisible) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = stringResource(R.string.stat_levels),
                    value = state.levelsCleared.toString(),
                    accent = LeafMint,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = stringResource(R.string.stat_best_run),
                    value = state.bestSeedsInRound.toString(),
                    accent = SunlitGold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Text(
            text = stringResource(zoneTitle(state.selectedZone)),
            style = MaterialTheme.typography.labelSmall,
            color = LeafMint,
            textAlign = TextAlign.Center
        )
        PrimaryButton(
            text = stringResource(R.string.action_play),
            onClick = onPlay,
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = stringResource(R.string.action_garden_atlas),
                onClick = onOpenAtlas,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.action_settings),
                onClick = onOpenSettings,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

private fun zoneChipLabel(zone: ZoneId): Int = when (zone) {
    ZoneId.MIST_BASIN -> R.string.chip_mist_basin
    ZoneId.FERN_HOLLOW -> R.string.chip_fern_hollow
    ZoneId.CANOPY_HEIGHTS -> R.string.chip_canopy_heights
    ZoneId.DAILY -> R.string.chip_daily
}
