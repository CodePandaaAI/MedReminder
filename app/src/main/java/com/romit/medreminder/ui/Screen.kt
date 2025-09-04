package com.romit.medreminder.ui

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object AddMedicineDetailsScreenFlow : Screen

    @Serializable
    data object AddMedicineDetailsScreen : Screen

    @Serializable
    data object AddMedicineDetailsScreenComplete : Screen
}