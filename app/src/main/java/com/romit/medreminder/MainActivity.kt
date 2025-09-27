package com.romit.medreminder

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
        createNotificationChannel()
        requestNotificationPermission()
        enableEdgeToEdge()
        setContent {
            MedReminderTheme {
                AppNavigation()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }
    private fun createNotificationChannel() {
        val channelId = "medicine_reminders_channel"
        val channelName = "Medication Reminders"
        val channelDescription = "This channel push medication reminder notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
            enableLights(true)
            enableVibration(true)
            setBypassDnd(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        // Register the channel with the system
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(channel)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isHome = currentDestination?.route == Screen.Home::class.qualifiedName
    val isEditMedicine = currentDestination?.route?.startsWith(Screen.EditMedicineScreen::class.qualifiedName.orEmpty()) == true
    val isAddMedicineFlow = currentDestination?.route == Screen.AddMedicineDetailsScreenFlow::class.qualifiedName
    val isAddMedicineDetails = currentDestination?.route == Screen.AddMedicineDetailsScreen::class.qualifiedName
    val isAddMedicineComplete = currentDestination?.route == Screen.AddMedicineDetailsScreenComplete::class.qualifiedName

    val title = when {
        isHome -> "MedReminder"
        isEditMedicine -> "Edit Medicine"
        isAddMedicineFlow || isAddMedicineDetails || isAddMedicineComplete -> "Add Medicine Details"
        else -> "MedReminder"
    }

    // FAB visibility - only show on Home screen
    val showFab = isHome

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    if (isSystemInDarkTheme()) Color(0xFF121212)
                    else MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        floatingActionButton = {
            if (showFab) {
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
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    onAddMedicineClicked = {
                        navController.navigate(Screen.AddMedicineDetailsScreenFlow)
                    },
                    onMedicineClicked = { medicineId ->
                        navController.navigate(Screen.EditMedicineScreen(id = medicineId))
                    }
                )
            }

            composable<Screen.EditMedicineScreen> { backStackEntry ->
                val routeArgs = backStackEntry.toRoute<Screen.EditMedicineScreen>()
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