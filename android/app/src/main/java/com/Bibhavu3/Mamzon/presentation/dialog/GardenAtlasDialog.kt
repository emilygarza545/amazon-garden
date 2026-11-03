package com.Bibhavu3.Mamzon.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.theme.DeepFoliage
import com.Bibhavu3.Mamzon.core.theme.LeafMint
import com.Bibhavu3.Mamzon.core.theme.MutedText
import com.Bibhavu3.Mamzon.core.theme.SageText
import com.Bibhavu3.Mamzon.core.theme.SunlitGold
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised
import com.Bibhavu3.Mamzon.core.theme.WarmCream
import com.Bibhavu3.Mamzon.core.ui.ClayPanel
import com.Bibhavu3.Mamzon.core.ui.SecondaryButton
import com.Bibhavu3.Mamzon.core.ui.decorationTitle
import com.Bibhavu3.Mamzon.core.ui.speciesArt
import com.Bibhavu3.Mamzon.domain.model.Decoration
import com.Bibhavu3.Mamzon.domain.model.PlantSpecies

@Composable
fun GardenAtlasDialog(
    species: List<PlantSpecies>,
    discovered: Set<String>,
    decorations: List<Decoration>,
    owned: Set<String>,
    equipped: String,
    totalSeeds: Int,
    onBuy: (String) -> Unit,
    onEquip: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        ClayPanel(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp),
            shape = RoundedCornerShape(30.dp),
            elevation = 24.dp,
            topColor = SurfaceRaised,
            bottomColor = DeepFoliage
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.atlas_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = WarmCream
                )
                Column(
                    modifier = Modifier
                        .heightIn(max = 340.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    species.forEach { item ->
                        SpeciesRow(species = item, found = item.id in discovered)
                    }
                    Text(
                        text = stringResource(R.string.atlas_restoration),
                        style = MaterialTheme.typography.labelSmall,
                        color = LeafMint
                    )
                    decorations.forEach { decoration ->
                        DecorationRow(
                            decoration = decoration,
                            owned = decoration.id in owned,
                            equipped = decoration.id == equipped,
                            affordable = totalSeeds >= decoration.cost,
                            onBuy = onBuy,
                            onEquip = onEquip
                        )
                    }
                }
                SecondaryButton(
                    text = stringResource(R.string.action_close),
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SpeciesRow(species: PlantSpecies, found: Boolean) {
    val art = speciesArt(species)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(art.image),
            contentDescription = stringResource(art.title),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(44.dp)
                .alpha(if (found) 1f else 0.25f)
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(art.title),
                style = MaterialTheme.typography.titleMedium,
                color = if (found) WarmCream else MutedText
            )
            Text(
                text = if (found) stringResource(art.description) else stringResource(R.string.atlas_not_found),
                style = MaterialTheme.typography.bodyLarge,
                color = if (found) SageText else MutedText
            )
        }
    }
}

@Composable
private fun DecorationRow(
    decoration: Decoration,
    owned: Boolean,
    equipped: Boolean,
    affordable: Boolean,
    onBuy: (String) -> Unit,
    onEquip: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.52f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = stringResource(decorationTitle(decoration)),
                style = MaterialTheme.typography.titleMedium,
                color = WarmCream
            )
            Text(
                text = stringResource(R.string.decoration_cost, decoration.cost),
                style = MaterialTheme.typography.labelSmall,
                color = SunlitGold
            )
        }
        SecondaryButton(
            text = when {
                equipped -> stringResource(R.string.action_equipped)
                owned -> stringResource(R.string.action_equip)
                else -> stringResource(R.string.action_buy)
            },
            onClick = {
                if (owned) onEquip(decoration.id) else onBuy(decoration.id)
            },
            enabled = owned || affordable,
            stateLabel = when {
                equipped -> stringResource(R.string.state_equipped)
                owned -> stringResource(R.string.state_owned)
                affordable -> stringResource(R.string.state_available)
                else -> stringResource(R.string.state_unavailable)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
