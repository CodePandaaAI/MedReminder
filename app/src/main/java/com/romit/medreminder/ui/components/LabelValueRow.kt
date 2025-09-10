package com.romit.medreminder.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun LabelValueRow(
    label: String,
    value: String,
    valueMaxLines: Int = 1, // Default to 1, can be overridden
    valueOverflow: TextOverflow = TextOverflow.Clip // Default, can be overridden
) {
    Row(verticalAlignment = Alignment.Top) { // Added verticalAlignment = Alignment.Top
        Text(label)
        Spacer(Modifier.width(4.dp)) // Add a small space between label and value
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            maxLines = valueMaxLines,
            overflow = valueOverflow
        )
    }
}