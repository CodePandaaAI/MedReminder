package com.romit.medreminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romit.medreminder.R
import com.romit.medreminder.ui.DosageType
import com.romit.medreminder.ui.viewmodels.AddMedicineScreenViewModel

@Composable
fun AddMedicineDetailsScreen(
    viewModel: AddMedicineScreenViewModel,
    modifier: Modifier,
    onFillNextDetailClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    val medUiState by viewModel.medicineUiState.collectAsState()
    val dailyDosage by remember(medUiState.dosage, medUiState.customDosage) {
        derivedStateOf {
            when (medUiState.dosage) {
                DosageType.OnceDaily -> 1
                DosageType.TwiceDaily -> 2
                DosageType.Custom -> medUiState.customDosage.toInt()
                DosageType.None -> 0
            }
        }
    }
    val fullWidthModifier = Modifier.fillMaxWidth()
    val buttonModifier = Modifier
        .height(72.dp)
        .width(144.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 32.dp, horizontal = 24.dp),
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
            shape = RoundedCornerShape(8.dp),
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

        OptionsWithRadioAndText(
            fullWidthModifier,
            medUiState.dosage == DosageType.OnceDaily,
            onOptionClicked = {
                viewModel.changeMedDosage(DosageType.OnceDaily)
            },
            "I eat it once a day!"
        )


        OptionsWithRadioAndText(
            fullWidthModifier,
            medUiState.dosage == DosageType.TwiceDaily,
            onOptionClicked = {
                viewModel.changeMedDosage(DosageType.TwiceDaily)
            },
            "I eat it twice a day!"
        )


        OptionsWithRadioAndText(
            fullWidthModifier,
            medUiState.dosage == DosageType.Custom,
            onOptionClicked = {
                viewModel.changeMedDosage(DosageType.Custom)
            },
            "Custom Dosage Amount"
        )

        if (medUiState.dosage == DosageType.Custom) {
            Slider(
                value = medUiState.customDosage,
                onValueChange = {
                    viewModel.changeCustomMedDosage(it)
                },
                valueRange = 0f..10f,
                steps = 10,
                modifier = Modifier
            )
            Text(text = "Selected value: $dailyDosage")
        }


        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
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
fun OptionsWithRadioAndText(
    modifier: Modifier,
    selectedRadioButton: Boolean, onOptionClicked: () -> Unit, optionText: String,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
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

@Composable
@Preview(showBackground = true)
fun PreviewAddMedPhaseFirst() {
    AddMedicineDetailsScreen(
        modifier = Modifier,
        onFillNextDetailClicked = {},
        onCancelClicked = {},
        viewModel = hiltViewModel()
    )
}
