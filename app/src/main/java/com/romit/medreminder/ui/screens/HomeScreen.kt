package com.romit.medreminder.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romit.medreminder.R
import com.romit.medreminder.data.local.entities.Medicine
import com.romit.medreminder.ui.theme.background
import com.romit.medreminder.ui.theme.foreground
import com.romit.medreminder.ui.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onAddMedicineClicked: () -> Unit,
    onMedicineClicked: (id: Long) -> Unit
) {
    val medicines by viewModel.allMedicines.collectAsState()

    // High-Level Container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) background else MaterialTheme.colorScheme.surfaceContainer)
    ) {
        // If No Medicines Show This
        if (medicines.isEmpty()) {
            EmptyState(onAddMedicineClicked = onAddMedicineClicked)
        }
        // If Have Medicines Show This
        else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Title For User Experience
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp) // ✅ 16 -> 20
                ) {
                    Text(
                        text = "My Medicines",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 29.sp), // ✅ Increased text size
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${medicines.size} medicines",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp), // ✅ Increased text size
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Medicine List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp), // ✅ 16 -> 20
                    verticalArrangement = Arrangement.spacedBy(10.dp) // ✅ 8 -> 10
                ) {
                    items(medicines) { medicine ->
                        // Medicine Card representing Each Medicine
                        MedicineCard(
                            medicine = medicine,
                            viewModel = viewModel,
                            onMedicineClicked = { onMedicineClicked(it) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(96.dp)) } // ✅ 80 -> 96
                }
            }

            // FAB
            FloatingActionButton(
                onClick = onAddMedicineClicked,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp) // ✅ 16 -> 20
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Medicine")
            }
        }
    }
}

@Composable
private fun MedicineCard(
    medicine: Medicine,
    viewModel: HomeScreenViewModel,
    onMedicineClicked: (id: Long) -> Unit
) {
    // Color For Remaining Refill Days based on how close or far is the day
    val refillColor = when {
        medicine.refillDays > 7 -> MaterialTheme.colorScheme.primary
        medicine.refillDays > 3 -> Color(0xFFFB8C00)
        else -> MaterialTheme.colorScheme.error
    }

    // Card with All details
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onMedicineClicked(medicine.medId) }),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) foreground
            else MaterialTheme.colorScheme.surface
        )
    ) {
        // Column the HIgh Level Container for Whole Card Elements
        Column(
            modifier = Modifier.padding(16.dp), // ✅ 16 -> 20
            verticalArrangement = Arrangement.spacedBy(12.dp) // ✅ 8 -> 10
        ) {
            // Row for Medicine Name and Refill Days Showcase
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medicine.name,
                        style = MaterialTheme.typography.titleLarge, // ✅ Increased text size
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${medicine.dosage} per day",
                        style = MaterialTheme.typography.bodyMedium, // ✅ Increased text size
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End, modifier = Modifier) {
                    Text(
                        text = "${medicine.refillDays}",
                        style = MaterialTheme.typography.titleLarge, // ✅ Increased text size
                        fontWeight = FontWeight.Bold,
                        color = refillColor
                    )
                    Text(
                        text = "days left",
                        style = MaterialTheme.typography.bodyMedium, // ✅ Increased text size
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Reminder Times
            val reminders = medicine.reminders.split(",")
                .map { viewModel.convertTo12HourFormat(it.trim()) }
            // Showing all reminders horizontally in surface
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // ✅ 6 -> 8
            ) {
                reminders.forEach { time ->
                    Surface(
                        shape = RoundedCornerShape(8.dp), // ✅ 6 -> 8
                        color = if (isSystemInDarkTheme()) background
                        else MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Text(
                            text = time,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 17.sp), // ✅ Increased text size
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            ) // ✅ 8,4 -> 10,5
                        )
                    }
                }
            }

            // Notes if they exists
            if (medicine.notes.isNotBlank()) {
                Text(
                    text = medicine.notes,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 17.sp), // ✅ Increased text size
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

// Shown when no medicines are stored
@Composable
private fun EmptyState(onAddMedicineClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(38.dp), // ✅ 32 -> 38
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.empty),
            contentDescription = null,
            modifier = Modifier
                .size(192.dp) // ✅ 160 -> 192
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(29.dp)) // ✅ 24 -> 29

        Text(
            text = "No medicines yet",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 27.sp), // ✅ Increased text size
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Add your first medicine to get started",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp), // ✅ Increased text size
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp) // ✅ 8 -> 10
        )

        Spacer(modifier = Modifier.height(38.dp)) // ✅ 32 -> 38

        FloatingActionButton(
            onClick = onAddMedicineClicked
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Medicine")
        }
    }
}