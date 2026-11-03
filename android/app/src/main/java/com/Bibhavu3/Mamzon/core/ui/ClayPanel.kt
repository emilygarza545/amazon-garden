package com.Bibhavu3.Mamzon.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Bibhavu3.Mamzon.core.theme.DeepFoliage
import com.Bibhavu3.Mamzon.core.theme.ShadowDeep
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised
import com.Bibhavu3.Mamzon.core.theme.WarmCream

@Composable
fun ClayPanel(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
    elevation: Dp = 18.dp,
    topColor: Color = SurfaceRaised,
    bottomColor: Color = DeepFoliage,
    borderAlpha: Float = 0.16f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = ShadowDeep,
                spotColor = ShadowDeep
            )
            .background(
                brush = Brush.verticalGradient(listOf(topColor, bottomColor)),
                shape = shape
            )
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        WarmCream.copy(alpha = borderAlpha),
                        Color.Transparent
                    )
                ),
                shape = shape
            ),
        content = content
    )
}
