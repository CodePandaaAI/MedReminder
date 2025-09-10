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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romit.medreminder.R
import com.romit.medreminder.ui.DosageType
import com.romit.medreminder.ui.components.TimePickerDialog
import com.romit.medreminder.ui.theme.background
import com.romit.medreminder.ui.theme.foreground
import com.romit.medreminder.ui.viewmodels.AddMedicineScreenViewModel
import kotlinx.coroutines.launch


@Composable
fun AddMedicineDetailsScreenComplete(
    viewmodel: AddMedicineScreenViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val medicineUiState by viewmodel.medicineUiState.collectAsState()
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val dailyDosage = remember(medicineUiState.dosage, medicineUiState.customDosage) {
        when (medicineUiState.dosage) {
            DosageType.OnceDaily -> 1
            DosageType.TwiceDaily -> 2
            DosageType.Custom -> medicineUiState.customDosage
            DosageType.None -> 0
        }
    }
    val fullWidthModifier = Modifier.fillMaxWidth()
    Column(
        modifier = Modifier.background(if (isSystemInDarkTheme()) background else MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp).fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            "Medicine Reminders!",
            style = MaterialTheme.typography.titleLarge
        )

        Surface(
            color =  if (isSystemInDarkTheme()) foreground
            else MaterialTheme.colorScheme.surface,
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
                        val success = viewmodel.addMedicine() // Now returns Boolean
                        if (success) {
                            onNavigateBack()
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to add medicine. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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

@Preview
@Composable
fun AddMedicinePhaseCompletePreview() {
    val viewmodel: AddMedicineScreenViewModel = hiltViewModel()
    AddMedicineDetailsScreenComplete(
        viewmodel = viewmodel,
        onNavigateBack = {})
}