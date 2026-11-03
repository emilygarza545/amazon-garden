package com.Bibhavu3.Mamzon.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Bibhavu3.Mamzon.core.theme.JungleGreen
import com.Bibhavu3.Mamzon.core.theme.LeafMint
import com.Bibhavu3.Mamzon.core.theme.MutedText
import com.Bibhavu3.Mamzon.core.theme.SageText
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised
import com.Bibhavu3.Mamzon.core.theme.WarmCream

@Composable
fun ZoneChip(
    label: String,
    selected: Boolean,
    locked: Boolean,
    stateLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container by animateColorAsState(
        targetValue = when {
            locked -> SurfaceRaised.copy(alpha = 0.6f)
            selected -> JungleGreen.copy(alpha = 0.28f)
            else -> SurfaceRaised.copy(alpha = 0.8f)
        },
        animationSpec = tween(durationMillis = 240),
        label = "zoneChipContainer"
    )
    val content = when {
        locked -> MutedText
        selected -> WarmCream
        else -> SageText
    }
    val shape = RoundedCornerShape(20.dp)
    TextButton(
        onClick = onClick,
        enabled = !locked,
        shape = shape,
        border = when {
            locked -> null
            selected -> BorderStroke(width = 1.5.dp, color = LeafMint)
            else -> BorderStroke(width = 1.dp, color = LeafMint.copy(alpha = 0.22f))
        },
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = SurfaceRaised.copy(alpha = 0.6f),
            disabledContentColor = MutedText
        ),
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .height(48.dp)
            .semantics { stateDescription = stateLabel }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.8.sp),
            color = if (locked) MutedText else Color.Unspecified,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
