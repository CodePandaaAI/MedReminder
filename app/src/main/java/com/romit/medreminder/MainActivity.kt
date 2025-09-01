package com.romit.medreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.romit.medreminder.ui.screens.AddMedicineScreenPhaseFirst
import com.romit.medreminder.ui.screens.HomeScreen
import com.romit.medreminder.ui.theme.MedReminderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedReminderTheme {
                AppNavigation()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val title = when(currentRoute) {
        "home" -> "MedReminder"
        else -> "Add Medicine Details"
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(title)
                }
            )
        },
        floatingActionButton = {
            if (currentRoute == "home") {
                FloatingActionButton(onClick = { navController.navigate("add_medicine_phase_first") }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "home") {
            composable(route = "home") {
                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onAddMedicineClicked = { navController.navigate("add_medicine_phase_first") }
                )
            }
            composable(route = "add_medicine_phase_first") {
                AddMedicineScreenPhaseFirst(
                    modifier = Modifier.padding(innerPadding),
                    onFillNextDetailClicked = {})
            }
            composable(route = "add_medicine_phase_final") {

            }
        }
    }
}