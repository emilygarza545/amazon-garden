package com.Bibhavu3.Mamzon.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.core.theme.DeepFoliage
import com.Bibhavu3.Mamzon.core.theme.EmberOrange
import com.Bibhavu3.Mamzon.core.theme.JungleGreen
import com.Bibhavu3.Mamzon.core.theme.MutedText
import com.Bibhavu3.Mamzon.core.theme.SageText
import com.Bibhavu3.Mamzon.core.theme.SurfaceRaised
import com.Bibhavu3.Mamzon.core.theme.WarmCream
import com.Bibhavu3.Mamzon.core.ui.ClayPanel
import com.Bibhavu3.Mamzon.core.ui.SecondaryButton

@Composable
fun SettingsDialog(
    soundEnabled: Boolean,
    hapticsEnabled: Boolean,
    hintsEnabled: Boolean,
    resetConfirmVisible: Boolean,
    onSoundChange: (Boolean) -> Unit,
    onHapticsChange: (Boolean) -> Unit,
    onHintsChange: (Boolean) -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        ClayPanel(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 520.dp),
            shape = RoundedCornerShape(30.dp),
            elevation = 24.dp,
            topColor = SurfaceRaised,
            bottomColor = DeepFoliage
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = WarmCream
                )
                SettingRow(
                    label = stringResource(R.string.settings_sound),
                    checked = soundEnabled,
                    onCheckedChange = onSoundChange
                )
                SettingRow(
                    label = stringResource(R.string.settings_haptics),
                    checked = hapticsEnabled,
                    onCheckedChange = onHapticsChange
                )
                SettingRow(
                    label = stringResource(R.string.settings_hints),
                    checked = hintsEnabled,
                    onCheckedChange = onHintsChange
                )
                if (resetConfirmVisible) {
                    Text(
                        text = stringResource(R.string.settings_reset_warning),
                        style = MaterialTheme.typography.bodyLarge,
                        color = SageText
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SecondaryButton(
                            text = stringResource(R.string.action_cancel),
                            onClick = onCancelReset,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.action_confirm_reset),
                            onClick = onConfirmReset,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    SecondaryButton(
                        text = stringResource(R.string.action_reset_progress),
                        onClick = onAskReset,
                        modifier = Modifier.fillMaxWidth()
                    )
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
private fun SettingRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val stateLabel = if (checked) {
        stringResource(R.string.state_on)
    } else {
        stringResource(R.string.state_off)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = WarmCream
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = WarmCream,
                checkedTrackColor = JungleGreen,
                uncheckedThumbColor = MutedText,
                uncheckedTrackColor = SurfaceRaised,
                uncheckedBorderColor = EmberOrange.copy(alpha = 0.35f)
            ),
            modifier = Modifier.semantics { stateDescription = stateLabel }
        )
    }
}
