package com.Bibhavu3.Mamzon.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.theme.DeepFoliage
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised
import com.Bibhavu3.Mamzon.core.ui.ClayPanel
import com.Bibhavu3.Mamzon.core.ui.SecondaryButton

@Composable
fun FloatingControlBar(
    hintEnabled: Boolean,
    onHint: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    ClayPanel(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(30.dp),
        elevation = 20.dp,
        topColor = SurfaceRaised,
        bottomColor = DeepFoliage,
        borderAlpha = 0.14f
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = stringResource(R.string.action_hint),
                onClick = onHint,
                enabled = hintEnabled,
                stateLabel = if (hintEnabled) {
                    stringResource(R.string.state_available)
                } else {
                    stringResource(R.string.state_unavailable)
                },
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.action_reset),
                onClick = onReset,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
