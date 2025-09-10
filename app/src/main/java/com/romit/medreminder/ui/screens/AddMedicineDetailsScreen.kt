package com.romit.medreminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romit.medreminder.R
import com.romit.medreminder.ui.DosageType
import com.romit.medreminder.ui.components.LabelValueRow
import com.romit.medreminder.ui.components.DosageOption
import com.romit.medreminder.ui.theme.background
import com.romit.medreminder.ui.theme.foreground
import com.romit.medreminder.ui.viewmodels.AddMedicineScreenViewModel

@Composable
fun AddMedicineDetailsScreen(
    viewModel: AddMedicineScreenViewModel,
    onFillNextDetailClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    val medUiState by viewModel.medicineUiState.collectAsState()
    val dailyDosage by remember(medUiState.dosage, medUiState.customDosage) {
        derivedStateOf {
            when (medUiState.dosage) {
                DosageType.OnceDaily -> 1
                DosageType.TwiceDaily -> 2
                DosageType.Custom -> medUiState.customDosage
                DosageType.None -> 0
            }
        }
    }
    val fullWidthModifier = Modifier.fillMaxWidth()
    val buttonModifier = Modifier
        .height(72.dp)
        .width(144.dp)

    Column(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) background else MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Whats The Medicine Name?",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
        )


        OutlinedTextField(
            value = medUiState.medName,
            onValueChange = { viewModel.changeMedName(it) },
            label = { Text("Enter Medicine Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            maxLines = 3,
            modifier = fullWidthModifier
        )

        Text(
            "Choose Daily Dosage!",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier
        )

        DosageOption(
            modifier = fullWidthModifier,
            medUiState.dosage == DosageType.OnceDaily,
            onOptionClicked = {
                viewModel.changeMedDosage(DosageType.OnceDaily)
            },
            "I eat it once a day!"
        )


        DosageOption(
            modifier = fullWidthModifier,
            medUiState.dosage == DosageType.TwiceDaily,
            onOptionClicked = {
                viewModel.changeMedDosage(DosageType.TwiceDaily)
            },
            "I eat it twice a day!"
        )


        DosageOption(
            modifier = fullWidthModifier,
            medUiState.dosage == DosageType.Custom,
            onOptionClicked = {
                viewModel.changeMedDosage(DosageType.Custom)
            },
            "Custom Dosage Amount"
        )

        if (medUiState.dosage == DosageType.Custom) {
            CustomDosageInput(
                customDosage = medUiState.customDosage,
                onDosageChange = { viewModel.changeCustomMedDosage(it) },
            )
        }

        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(28.dp),
            modifier = fullWidthModifier
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                LabelValueRow(
                    label = stringResource(id = R.string.medicine_name_label),
                    value = medUiState.medName,
                    valueMaxLines = 3,
                    valueOverflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                LabelValueRow(
                    label = stringResource(id = R.string.daily_dosage_label),
                    value = "${medUiState.dosage} -> $dailyDosage"
                )
            }
        }

        Row(
            modifier = fullWidthModifier,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = { onCancelClicked() }, modifier = buttonModifier
            ) {
                Text("Cancel")
            }
            Button(
                onClick = { onFillNextDetailClicked() }, modifier = buttonModifier,
                enabled = medUiState.medName.isNotBlank() && medUiState.dosage != DosageType.None
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun CustomDosageInput(
    customDosage: Int,
    onDosageChange: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    if (isSystemInDarkTheme()) foreground
                    else MaterialTheme.colorScheme.surface
                )
        ) {
            IconButton(
                onClick = {
                    if (customDosage > 1) {
                        onDosageChange(customDosage - 1)
                    }
                },
                enabled = customDosage > 1
            ) {
                Icon(painterResource(R.drawable.remove), contentDescription = "Decrease dosage")
            }
        }
        Spacer(Modifier.width(8.dp))
        OutlinedTextField(
            value = customDosage.toString(),
            onValueChange = { newValue ->
                // Allow only digits and handle empty string
                if (newValue.all { it.isDigit() }) {
                    val intValue = newValue.toIntOrNull() ?: 0
                    onDosageChange(intValue)
                }
            },
            label = { Text("Times a day") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier.width(120.dp)
        )
        Spacer(Modifier.width(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    if (isSystemInDarkTheme()) foreground
                    else MaterialTheme.colorScheme.surface
                )
        ) {
            IconButton(onClick = {
                onDosageChange(customDosage + 1)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Increase dosage")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAddMedPhaseFirst() {
    AddMedicineDetailsScreen(
        onFillNextDetailClicked = {},
        onCancelClicked = {},
        viewModel = hiltViewModel()
    )
}
