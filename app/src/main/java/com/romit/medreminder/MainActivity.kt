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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation // Import for nested navigation
import com.romit.medreminder.ui.screens.AddMedicinePhaseComplete
import com.romit.medreminder.ui.screens.AddMedicineScreenPhaseFirst
import com.romit.medreminder.ui.screens.HomeScreen
import com.romit.medreminder.ui.theme.MedReminderTheme
import com.romit.medreminder.ui.viewmodels.AddMedicineScreenViewModel
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

const val ADD_MEDICINE_FLOW_ROUTE = "add_medicine_flow"
const val ADD_MEDICINE_PHASE_FIRST_ROUTE = "add_medicine_phase_first"
const val ADD_MEDICINE_PHASE_FINAL_ROUTE = "add_medicine_phase_final"
const val HOME_ROUTE = "home"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when (navBackStackEntry?.destination?.parent?.route) { // Check parent for nested routes
        ADD_MEDICINE_FLOW_ROUTE -> "Add Medicine Details"
        else -> when (currentRoute) {
            HOME_ROUTE -> "MedReminder"
            // If we are directly on a route that is part of the flow (e.g. during transitions or deep links)
            ADD_MEDICINE_PHASE_FIRST_ROUTE, ADD_MEDICINE_PHASE_FINAL_ROUTE -> "Add Medicine Details"
            else -> "MedReminder" // Default title
        }
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
            if (currentRoute == HOME_ROUTE) {
                FloatingActionButton(onClick = { navController.navigate(ADD_MEDICINE_FLOW_ROUTE) }) { // Navigate to the flow
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Medicine")
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = HOME_ROUTE) {
            composable(route = HOME_ROUTE) {
                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onAddMedicineClicked = { navController.navigate(ADD_MEDICINE_FLOW_ROUTE) } // Navigate to the flow
                )
            }

            // Nested navigation graph for the add medicine flow
            navigation(
                startDestination = ADD_MEDICINE_PHASE_FIRST_ROUTE,
                route = ADD_MEDICINE_FLOW_ROUTE
            ) {
                composable(route = ADD_MEDICINE_PHASE_FIRST_ROUTE) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(ADD_MEDICINE_FLOW_ROUTE)
                    }
                    val addMedicineScreenViewModel: AddMedicineScreenViewModel = hiltViewModel(parentEntry)

                    AddMedicineScreenPhaseFirst(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = addMedicineScreenViewModel,
                        onFillNextDetailClicked = { navController.navigate(ADD_MEDICINE_PHASE_FINAL_ROUTE) },
                        onCancelClicked = { navController.popBackStack(ADD_MEDICINE_FLOW_ROUTE, inclusive = true) }
                    )
                }
                composable(route = ADD_MEDICINE_PHASE_FINAL_ROUTE) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(ADD_MEDICINE_FLOW_ROUTE)
                    }
                    val addMedicineScreenViewModel: AddMedicineScreenViewModel = hiltViewModel(parentEntry)

                    AddMedicinePhaseComplete(
                        viewmodel = addMedicineScreenViewModel,
                        modifier = Modifier.padding(innerPadding),
//                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}