package com.Bibhavu3.Mamzon.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.theme.DeepFoliage
import com.Bibhavu3.Mamzon.core.theme.SageText
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised
import com.Bibhavu3.Mamzon.core.theme.WarmCream
import com.Bibhavu3.Mamzon.core.ui.ClayPanel
import com.Bibhavu3.Mamzon.core.ui.PrimaryButton
import com.Bibhavu3.Mamzon.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    levelLabel: String,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onQuit: () -> Unit
) {
    Dialog(onDismissRequest = onResume) {
        ClayPanel(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp),
            elevation = 24.dp,
            topColor = SurfaceRaised,
            bottomColor = DeepFoliage
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.pause_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = WarmCream
                )
                Text(
                    text = levelLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = SageText
                )
                PrimaryButton(
                    text = stringResource(R.string.action_resume),
                    onClick = onResume,
                    modifier = Modifier.fillMaxWidth()
                )
                SecondaryButton(
                    text = stringResource(R.string.action_restart_level),
                    onClick = onRestart,
                    modifier = Modifier.fillMaxWidth()
                )
                SecondaryButton(
                    text = stringResource(R.string.action_quit_to_menu),
                    onClick = onQuit,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
