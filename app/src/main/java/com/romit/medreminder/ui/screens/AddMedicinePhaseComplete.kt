package com.romit.medreminder.ui.screens

import android.icu.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.romit.medreminder.R

@Composable
fun AddMedicinePhaseComplete(modifier: Modifier) {
    var selectedTimes by remember { mutableStateOf(mutableListOf<String>()) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Medicine Reminder Times",
            style = MaterialTheme.typography.headlineSmall
        )

        // Selected times list
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (selectedTimes.isEmpty()) {
                    Text(
                        text = "No reminder times set",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Selected Times (${selectedTimes.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    selectedTimes.forEachIndexed { index, time ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(painter = painterResource(R.drawable.access_time), contentDescription = null)
                            Text(
                                text = convertTo12HourFormat(time),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            IconButton(
                                onClick = {
                                    selectedTimes = selectedTimes.toMutableList().apply {
                                        removeAt(index)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        // Add time button
        Button(
            onClick = { showTimePickerDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Add Reminder Time")
        }

        if (showTimePickerDialog) {
            TimePickerDialog(
                onConfirm = { hour, minute ->
                    val newTime = String.format("%02d:%02d", hour, minute)
                    if (!selectedTimes.contains(newTime)) {
                        selectedTimes = selectedTimes.toMutableList().apply {
                            add(newTime)
                            sort()
                        }
                    }
                    showTimePickerDialog = false
                },
                onDismiss = { showTimePickerDialog = false }
            )
        }
    }
}

// Simplified conversion function
fun convertTo12HourFormat(time: String): String {
    val (hourStr, minuteStr) = time.split(":")
    val hour = hourStr.toInt()

    val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    val amPm = if (hour < 12) "AM" else "PM"

    return "$displayHour:$minuteStr $amPm"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    var showDial by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Time",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                if (showDial) {
                    TimePicker(state = timePickerState)
                } else {
                    TimeInput(state = timePickerState)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showDial = !showDial }) {
                        Icon(
                            painter = painterResource(
                                if (showDial) R.drawable.edit_calendar
                                else R.drawable.access_time
                            ),
                            contentDescription = "Toggle"
                        )
                    }

                    Row {
                        TextButton(onClick = onDismiss) { Text("Cancel") }
                        TextButton(onClick = {
                            onConfirm(timePickerState.hour, timePickerState.minute)
                        }) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}