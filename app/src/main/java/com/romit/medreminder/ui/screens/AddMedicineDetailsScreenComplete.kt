package com.romit.medreminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.romit.medreminder.R
import com.romit.medreminder.ui.DosageType
import com.romit.medreminder.ui.viewmodels.AddMedicineScreenViewModel
import kotlinx.coroutines.launch


@Composable
fun AddMedicineDetailsScreenComplete(
    modifier: Modifier,
    viewmodel: AddMedicineScreenViewModel,
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val medicineUiState by viewmodel.medicineUiState.collectAsState()
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val dailyDosage = remember(medicineUiState.dosage, medicineUiState.customDosage) {
        when (medicineUiState.dosage) {
            DosageType.OnceDaily -> 1
            DosageType.TwiceDaily -> 2
            DosageType.Custom -> medicineUiState.customDosage.toInt()
            DosageType.None -> 0
        }
    }
    val fullWidthModifier = Modifier.fillMaxWidth()
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Medicine Reminders!",
            style = MaterialTheme.typography.titleLarge
        )

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = fullWidthModifier,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = fullWidthModifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (medicineUiState.reminders.isEmpty()) {
                    Text("No Reminder Added")
                } else {
                    medicineUiState.reminders.forEachIndexed { index, time ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = fullWidthModifier
                                .padding(vertical = 4.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.access_time),
                                contentDescription = null
                            )
                            Text(viewmodel.convertTo12HourFormat(time))
                            IconButton(onClick = { viewmodel.removeReminder(index) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { showTimePickerDialog = true },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            modifier = fullWidthModifier,
            enabled = medicineUiState.reminders.size < dailyDosage
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Reminders")
            Spacer(Modifier.size(ButtonDefaults.IconSize))
            Text("Add Reminders!")

        }
        Spacer(Modifier.height(8.dp))
        Text(
            "Refill Reminder",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            "Set how often you'll need to refill this medicine.",
            style = MaterialTheme.typography.bodyMedium
        )
        OutlinedTextField(
            value = if (medicineUiState.refillDays == 0) "" else medicineUiState.refillDays.toString(),
            onValueChange = { viewmodel.validateAndChangeMedicineRefillDays(it) },
            modifier = fullWidthModifier,
            label = { Text("Days until refill") },
            placeholder = { Text("e.g., 30") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true
        )

        Text("Any Note about Medicine? (Optional)")
        OutlinedTextField(
            value = medicineUiState.notes,
            onValueChange = { viewmodel.addMedicineNote(it) },
            modifier = fullWidthModifier,
            label = { Text("Notes") },
            placeholder = { Text("e.g., Take after food") },
            maxLines = 3
        )
        Button(
            onClick = {
                if (medicineUiState.reminders.isNotEmpty()) {
                    scope.launch {
                        viewmodel.addMedicine()
                    }
                    onNavigateBack()
                }
            },
            enabled = medicineUiState.reminders.isNotEmpty(),
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            modifier = fullWidthModifier.height(72.dp)
        ) {
            Icon(painterResource(R.drawable.medication), contentDescription = null)
            Spacer(Modifier.size(ButtonDefaults.IconSize))
            Text("Add Medicine!")
        }
    }
    if (showTimePickerDialog) {
        TimePickerDialog(onDismiss = { showTimePickerDialog = false }, onConfirm = { hour, minute ->
            viewmodel.validateAndAddReminder(hour, minute)
            showTimePickerDialog = false
        }
        )
    }
}

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

@Preview
@Composable
fun AddMedicinePhaseCompletePreview() {
    val viewmodel: AddMedicineScreenViewModel = hiltViewModel()
    AddMedicineDetailsScreenComplete(
        modifier = Modifier,
        viewmodel = viewmodel,
        onNavigateBack = {})
}