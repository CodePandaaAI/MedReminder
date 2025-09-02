package com.romit.medreminder.ui.screens

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.romit.medreminder.R
import com.romit.medreminder.ui.DosageType
import com.romit.medreminder.ui.viewmodels.AddMedicineScreenViewModel

@Composable
fun AddMedicinePhaseComplete(
    modifier: Modifier,
    viewmodel: AddMedicineScreenViewModel
) {

    val medUiState by viewmodel.medicineUiState.collectAsState()
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val dailyDosage = remember(medUiState.dosage, medUiState.customDosage) { // Ensure recalculation when relevant state changes
        when (medUiState.dosage) {
            DosageType.OnceDaily -> 1
            DosageType.TwiceDaily -> 2
            DosageType.Custom -> medUiState.customDosage.toInt() // Handle potential parsing error
            DosageType.None -> 0
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp).verticalScroll(rememberScrollState()),
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
                if (medUiState.reminders.isEmpty()) {
                    Text(
                        text = "No reminder times set",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Selected Times (${medUiState.reminders.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    medUiState.reminders.forEachIndexed { index, time ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.access_time),
                                contentDescription = null
                            )
                            Text(
                                text = viewmodel.convertTo12HourFormat(time),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            IconButton(
                                onClick = {
                                    viewmodel.removeReminder(index)
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
        LaunchedEffect(medUiState, dailyDosage) {
            Log.d(
                "AddMedDebug",
                "DosageType: ${medUiState.dosage}, CustomDosage: ${medUiState.customDosage}, Calculated dailyDosage: $dailyDosage, Reminders count: ${medUiState.reminders.size}"
            )
        }
        // Add time button
        Button(
            onClick = { showTimePickerDialog = true },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            enabled = medUiState.reminders.size < dailyDosage // Updated condition
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.size(ButtonDefaults.IconSize))
            Text("Add Reminder Time")
        }

        if (showTimePickerDialog) {
            TimePickerDialog(
                onConfirm = { hour, minute ->
                    viewmodel.validateAndAddReminder(hour, minute)
                    showTimePickerDialog = false
                },
                onDismiss = { showTimePickerDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY), // Default to current time
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showDial = !showDial }) {
                        Icon(
                            painter = painterResource(
                                if (showDial) R.drawable.edit_calendar
                                else R.drawable.access_time
                            ),
                            contentDescription = "Toggle Input Mode" // Added content description
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
