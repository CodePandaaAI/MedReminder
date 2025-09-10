package com.romit.medreminder.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun DosageOption(
    modifier: Modifier,
    selectedRadioButton: Boolean, onOptionClicked: () -> Unit, optionText: String,
) {
    val foreground = Color(0xFF1E1E1E)
    Surface(
        color = if (isSystemInDarkTheme()) foreground
        else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .selectable(
                selected = selectedRadioButton, onClick = {
                    onOptionClicked()
                }, role = Role.RadioButton
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            RadioButton(
                selected = selectedRadioButton, onClick = null
            )
            Text(optionText)
        }
    }
}