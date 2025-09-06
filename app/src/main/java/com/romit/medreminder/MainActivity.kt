package com.romit.medreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.romit.medreminder.ui.Screen
import com.romit.medreminder.ui.screens.AddMedicineDetailsScreenComplete
import com.romit.medreminder.ui.screens.AddMedicineDetailsScreen
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Get title based on current route using serializable objects
    val title = when {
        currentDestination?.route == Screen.Home::class.qualifiedName -> "MedReminder"
        currentDestination?.parent?.route == Screen.AddMedicineDetailsScreenFlow::class.qualifiedName -> "Add Medicine Details"
        currentDestination?.route == Screen.AddMedicineDetailsScreen::class.qualifiedName -> "Add Medicine Details"
        currentDestination?.route == Screen.AddMedicineDetailsScreenComplete::class.qualifiedName -> "Add Medicine Details"
        else -> "MedReminder"
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
            // Show FAB only on Home screen
            if (currentDestination?.route == Screen.Home::class.qualifiedName) {
                ExtendedFloatingActionButton(onClick = {
                    navController.navigate(Screen.AddMedicineDetailsScreenFlow)
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Medicine")
                    Text("Add Medicine")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onAddMedicineClicked = {
                        navController.navigate(Screen.AddMedicineDetailsScreenFlow)
                    }
                )
            }

            // Nested navigation graph for the add medicine flow
            navigation<Screen.AddMedicineDetailsScreenFlow>(
                startDestination = Screen.AddMedicineDetailsScreen
            ) {
                composable<Screen.AddMedicineDetailsScreen> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry<Screen.AddMedicineDetailsScreenFlow>()
                    }
                    val addMedicineScreenViewModel: AddMedicineScreenViewModel =
                        hiltViewModel(parentEntry)

                    AddMedicineDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
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

                composable<Screen.AddMedicineDetailsScreenComplete> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry<Screen.AddMedicineDetailsScreenFlow>()
                    }
                    val addMedicineScreenViewModel: AddMedicineScreenViewModel =
                        hiltViewModel(parentEntry)

                    AddMedicineDetailsScreenComplete(
                        viewmodel = addMedicineScreenViewModel,
                        modifier = Modifier.padding(innerPadding),
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