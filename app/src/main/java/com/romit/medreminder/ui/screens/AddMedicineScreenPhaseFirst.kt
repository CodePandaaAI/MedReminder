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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(
            painterResource(R.drawable.excited),
            contentDescription = null,
            modifier = Modifier
                .size(192.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            "Let's Add Your Medicine!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Whats The Medicine Name?",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
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
        Spacer(Modifier.height(16.dp))
        Text(
            "Choose Daily Dosage!",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .selectable(
                        selected = radioOnceDay,
                        onClick = {
                            radioOnceDay = true
                            radioCustom = false
                            radioTwiceDay = false
                        },
                        role = Role.RadioButton
                    )
            ) {
                RadioButton(
                    selected = radioOnceDay, onClick = null
                )
                Text("I eat it once a day!")
            }
        }
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .selectable(
                        selected = radioTwiceDay,
                        onClick = {
                            radioOnceDay = false
                            radioCustom = false
                            radioTwiceDay = true
                        },
                        role = Role.RadioButton
                    )
            ) {
                RadioButton(selected = radioTwiceDay, onClick = null)
                Text("I eat it twice a day!")
            }
        }
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .selectable(
                        selected = radioCustom,
                        onClick = {
                            radioOnceDay = false
                            radioCustom = true
                            radioTwiceDay = false
                        },
                        role = Role.RadioButton
                    )
            ) {
                RadioButton(selected = radioCustom, onClick = null)
                Text("Custom Dosage Amount")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAddMedPhaseFirst() {
    AddMedicineScreenPhaseFirst(modifier = Modifier) {}
}