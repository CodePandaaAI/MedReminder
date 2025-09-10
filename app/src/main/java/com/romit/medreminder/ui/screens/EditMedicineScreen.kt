package com.romit.medreminder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romit.medreminder.R
import com.romit.medreminder.ui.DosageType
import com.romit.medreminder.ui.components.DosageOption
import com.romit.medreminder.ui.components.TimePickerDialog
import com.romit.medreminder.ui.theme.background
import com.romit.medreminder.ui.theme.foreground
import com.romit.medreminder.ui.viewmodels.EditMedicineScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun EditMedicineScreen(
    medId: Long,
    onCancelOrSuccess: () -> Unit,
    viewModel: EditMedicineScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(medId) {
        viewModel.getMedicineByIdAndUpdateUIState(medId)
    }

    val medicineUiState by viewModel.medicineUiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val buttonModifier = Modifier
        .height(72.dp)
        .width(144.dp)

    val dailyDosage by remember(medicineUiState.dosage, medicineUiState.customDosage) {
        derivedStateOf {
            when (medicineUiState.dosage) {
                DosageType.OnceDaily -> 1
                DosageType.TwiceDaily -> 2
                DosageType.Custom -> medicineUiState.customDosage
                DosageType.None -> 0
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) background else MaterialTheme.colorScheme.surfaceContainer)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Medicine Name
        OutlinedTextField(
            value = medicineUiState.medName,
            onValueChange = { viewModel.changeMedName(it) },
            label = { Text("Medicine Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        // Daily Dosage Section
        Text(
            text = "Daily Dosage",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )


        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DosageOption(
                modifier = Modifier.fillMaxWidth(),
                selectedRadioButton = medicineUiState.dosage == DosageType.OnceDaily,
                onOptionClicked = { viewModel.changeMedDosage(DosageType.OnceDaily) },
                optionText = "Once daily"
            )

            DosageOption(
                modifier = Modifier.fillMaxWidth(),
                selectedRadioButton = medicineUiState.dosage == DosageType.TwiceDaily,
                onOptionClicked = { viewModel.changeMedDosage(DosageType.TwiceDaily) },
                optionText = "Twice daily"
            )

            DosageOption(
                modifier = Modifier.fillMaxWidth(),
                selectedRadioButton = medicineUiState.dosage == DosageType.Custom,
                onOptionClicked = { viewModel.changeMedDosage(DosageType.Custom) },
                optionText = "Custom amount"
            )

            if (medicineUiState.dosage == DosageType.Custom) {
                OutlinedTextField(
                    value = if (medicineUiState.customDosage == 0) "" else medicineUiState.customDosage.toString(),
                    onValueChange = {
                        val dosage = it.toIntOrNull() ?: 0
                        viewModel.changeCustomMedDosage(dosage)
                    },
                    label = { Text("Times per day") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }


        // Reminder Times Section
        Text(
            text = "Reminder Times",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isSystemInDarkTheme()) foreground
                else MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (medicineUiState.reminders.isEmpty()) {
                    Text(
                        text = "No reminders added",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    medicineUiState.reminders.forEachIndexed { index, time ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.access_time),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = viewModel.convertTo12HourFormat(time),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            IconButton(onClick = { viewModel.removeReminder(index) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Remove reminder",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = { showTimePickerDialog = true },
                    enabled = medicineUiState.reminders.size < dailyDosage,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.size(8.dp))
                    Text("Add Reminder Time")
                }
            }
        }

        // Refill Days
        Text(
            text = "Refill Reminder",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        OutlinedTextField(
            value = if (medicineUiState.refillDays == 0) "" else medicineUiState.refillDays.toString(),
            onValueChange = { viewModel.validateAndChangeMedicineRefillDays(it) },
            label = { Text("Days until refill") },
            placeholder = { Text("e.g., 30") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Notes
        OutlinedTextField(
            value = medicineUiState.notes,
            onValueChange = { viewModel.addMedicineNote(it) },
            label = { Text("Notes (Optional)") },
            placeholder = { Text("e.g., Take after food") },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancelOrSuccess,
                modifier = buttonModifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (medicineUiState.reminders.isNotEmpty()) {
                        scope.launch {
                            val success = viewModel.updateMedicine(medId)
                            if (success) {
                                onCancelOrSuccess()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to update medicine. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                enabled = medicineUiState.reminders.isNotEmpty() && medicineUiState.medName.isNotBlank(),
                modifier = buttonModifier.weight(1f)
            ) {
                Text("Save Changes")
            }
        }

        Spacer(modifier = Modifier.height(80.dp)) // Bottom padding
    }

    if (showTimePickerDialog) {
        TimePickerDialog(
            onDismiss = { showTimePickerDialog = false },
            onConfirm = { hour, minute ->
                viewModel.validateAndAddReminder(hour, minute)
                showTimePickerDialog = false
            }
        )
    }
}