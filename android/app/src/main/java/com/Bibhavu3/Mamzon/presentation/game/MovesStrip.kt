package com.Bibhavu3.Mamzon.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.config.GameConfig
import com.Bibhavu3.Mamzon.core.theme.CanopyMint
import com.Bibhavu3.Mamzon.core.theme.EmberOrange
import com.Bibhavu3.Mamzon.core.theme.SunlitGold
import com.Bibhavu3.Mamzon.core.theme.WarmCream

@Composable
fun MovesStrip(
    movesLeft: Int,
    movesAllowed: Int,
    hydrated: Int,
    required: Int,
    modifier: Modifier = Modifier
) {
    val movesColor = when {
        movesLeft <= 1 -> EmberOrange
        movesLeft <= GameConfig.LOW_MOVES_WARNING -> SunlitGold
        else -> WarmCream
    }
    val status = stringResource(R.string.game_status, movesLeft, hydrated, required)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 24.dp)
            .semantics {
                liveRegion = LiveRegionMode.Polite
                stateDescription = status
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.game_moves, movesLeft, movesAllowed),
            style = MaterialTheme.typography.titleMedium,
            color = movesColor
        )
        Text(
            text = stringResource(R.string.game_plants, hydrated, required),
            style = MaterialTheme.typography.titleMedium,
            color = CanopyMint
        )
    }
}
