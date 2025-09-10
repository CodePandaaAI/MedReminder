package com.romit.medreminder.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.romit.medreminder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(onConfirm: (hour: Int, minute: Int) -> Unit, onDismiss: () -> Unit) {
    val timePickerState = rememberTimePickerState(
        initialMinute = 2,
        initialHour = 2,
        is24Hour = false
    )
    var showDialog by remember { mutableStateOf(true) }
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Set Time")
                if (showDialog) {
                    TimePicker(state = timePickerState)
                } else {
                    TimeInput(state = timePickerState)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { showDialog = !showDialog }) {
                        Icon(
                            painter = if (showDialog) painterResource(R.drawable.keyboard)
                            else painterResource(R.drawable.access_time),
                            contentDescription = null
                        )
                    }
                    Row {
                        TextButton(onClick = { onDismiss() }) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            onConfirm(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                        }) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}