package com.romit.medreminder.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romit.medreminder.R

@Composable
fun AddMedicineScreenPhaseFirst(modifier: Modifier, onFillNextDetailClicked: () -> Unit) {
    var medName by remember { mutableStateOf("") }
    var radioOnceDay by remember { mutableStateOf(false) }
    var radioTwiceDay by remember { mutableStateOf(false) }
    var radioCustom by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painterResource(R.drawable.excited),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(128.dp)
            )
        }
        item {
            Text(
                "Let's Add Your Medicine!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
        }
        item {
            Text(
                "Whats The Medicine Name?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        item {
            OutlinedTextField(
                value = medName,
                onValueChange = { medName = it },
                shape = RoundedCornerShape(8.dp),
                label = { Text("Enter Medicine Name") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        }

        item {
            Text(
                "Choose Daily Dosage!",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        item {
            OptionsWithRadioAndText(radioOnceDay, onOptionClicked = {
                radioOnceDay = true
                radioCustom = false
                radioTwiceDay = false
                sliderPosition = 0f
            }, "I eat it once a day!")
        }
        item {
            OptionsWithRadioAndText(radioTwiceDay, onOptionClicked = {
                radioOnceDay = false
                radioCustom = false
                radioTwiceDay = true
                sliderPosition = 0f
            }, "I eat it twice a day!")
        }
        item {
            OptionsWithRadioAndText(radioCustom, onOptionClicked = {
                radioOnceDay = false
                radioCustom = true
                radioTwiceDay = false
            }, "Custom Dosage Amount")
        }
        if (radioCustom) {
            item {
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..20f, // Define the range from 0 to 100,
                    steps = 20,
                    modifier = Modifier.padding(horizontal = 56.dp)
                )
                Text(text = "Selected value: ${sliderPosition.toInt()}")
            }
        }
        item {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Medicine Name: $medName", fontWeight = FontWeight.Bold)
                    Text(
                        "Daily Dosage: ${
                            when {
                                radioCustom -> sliderPosition.toInt()
                                radioOnceDay -> 1
                                else -> 2
                            }
                        }"
                    )
                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = {}, modifier = Modifier.height(72.dp).width(144.dp)) {
                    Text("Cancel")
                }
                Button(onClick = {}, modifier = Modifier.height(72.dp).width(144.dp)) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun OptionsWithRadioAndText(
    selectedRadioButton: Boolean,
    onOptionClicked: () -> Unit,
    optionText: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .selectable(
                selected = selectedRadioButton,
                onClick = {
                    onOptionClicked()
                },
                role = Role.RadioButton
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
        ) {
            RadioButton(
                selected = selectedRadioButton, onClick = null
            )
            Text(optionText)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAddMedPhaseFirst() {
    AddMedicineScreenPhaseFirst(modifier = Modifier) {}
}
