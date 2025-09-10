package com.romit.medreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.romit.medreminder.ui.screens.AddMedicineDetailsScreen
import com.romit.medreminder.ui.screens.AddMedicineDetailsScreenComplete
import com.romit.medreminder.ui.screens.EditMedicineScreen
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val title = when (navController.currentBackStackEntry?.toRoute<Screen>()) {
        is Screen.Home -> "MedReminder"
        is Screen.EditMedicineScreen -> "Edit Medicine" // Correctly handles EditMedicineScreen title
        is Screen.AddMedicineDetailsScreenFlow, // Covers the flow itself if it were a direct destination
        is Screen.AddMedicineDetailsScreen,
        is Screen.AddMedicineDetailsScreenComplete -> "Add Medicine Details"
        else -> "MedReminder" // Default title
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    if (isSystemInDarkTheme()) Color(0xFF121212) else MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        floatingActionButton = {
            // Simplified FAB visibility using type-safe check
            if (navController.currentBackStackEntry?.toRoute<Screen>() is Screen.Home) {
                ExtendedFloatingActionButton(onClick = {
                    navController.navigate(Screen.AddMedicineDetailsScreenFlow)
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Medicine")
                    Text(text = "Add Medicine")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home, // Assumes Screen.Home is a @Serializable object or data object
            modifier = Modifier.padding(innerPadding) // Apply padding from Scaffold to NavHost
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    // Modifier.padding(innerPadding) is now handled by NavHost
                    onAddMedicineClicked = {
                        navController.navigate(Screen.AddMedicineDetailsScreenFlow)
                    },
                    onMedicineClicked = { medicineId -> // medicineId is Long
                        navController.navigate(Screen.EditMedicineScreen(id = medicineId))
                    }
                )
            }

            composable<Screen.EditMedicineScreen> { backStackEntry ->
                val routeArgs = backStackEntry.toRoute<Screen.EditMedicineScreen>()
                // EditMedicineScreen only needs the ID for now
                EditMedicineScreen(
                    medId = routeArgs.id,
                    onCancelOrSuccess = {
                        navController.popBackStack<Screen.Home>(
                            inclusive = false,
                            saveState = false
                        )
                    }
                )
            }

            navigation<Screen.AddMedicineDetailsScreenFlow>(
                startDestination = Screen.AddMedicineDetailsScreen
            ) {
                composable<Screen.AddMedicineDetailsScreen> { navBackStackEntry ->
                    val parentEntry = remember(navBackStackEntry) {
                        navController.getBackStackEntry<Screen.AddMedicineDetailsScreenFlow>()
                    }
                    val addMedicineScreenViewModel: AddMedicineScreenViewModel =
                        hiltViewModel(parentEntry)

                    AddMedicineDetailsScreen(
                        // Modifier.padding(innerPadding) is now handled by NavHost
                        viewModel = addMedicineScreenViewModel,
                        onFillNextDetailClicked = {
                            navController.navigate(Screen.AddMedicineDetailsScreenComplete)
                        },
                        onCancelClicked = {
                            navController.popBackStack<Screen.AddMedicineDetailsScreenFlow>(
                                inclusive = true
                            )
                        }
                    )
                }

                composable<Screen.AddMedicineDetailsScreenComplete> { navBackStackEntry ->
                    val parentEntry = remember(navBackStackEntry) {
                        navController.getBackStackEntry<Screen.AddMedicineDetailsScreenFlow>()
                    }
                    val addMedicineScreenViewModel: AddMedicineScreenViewModel =
                        hiltViewModel(parentEntry)

                    AddMedicineDetailsScreenComplete(
                        // Modifier.padding(innerPadding) is now handled by NavHost
                        viewmodel = addMedicineScreenViewModel,
                        onNavigateBack = {
                            navController.popBackStack<Screen.Home>(
                                inclusive = false,
                                saveState = false
                            )
                        }
                    )
                }
            }
        }
    }
}