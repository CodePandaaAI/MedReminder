package com.romit.medreminder.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romit.medreminder.R
import com.romit.medreminder.ui.theme.MedReminderTheme
import com.romit.medreminder.ui.viewmodels.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onAddMedicineClicked: () -> Unit
) {
    val medicines by viewModel.allMedicines.collectAsState()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (medicines.isEmpty()) {
            Image(
                painter = painterResource(R.drawable.not_found),
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )
            Text("No Reports Yet!")
            Button(onClick = { onAddMedicineClicked() }) {
                Text("Add Medicines")
            }
        } else {
            LazyColumn {
                items(medicines) { medicine ->
                    Card {
                        Column(
                            Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(medicine.name)
                            if (medicine.notes != null) {
                                Text(medicine.notes)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() = MedReminderTheme { HomeScreen(onAddMedicineClicked = {}) }