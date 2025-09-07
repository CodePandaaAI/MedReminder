package com.romit.medreminder

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object AddMedicineDetailsScreenFlow : Screen

    @Serializable
    data object AddMedicineDetailsScreen : Screen

    @Serializable
    data object AddMedicineDetailsScreenComplete : Screen

    @Serializable
    data class EditMedicineScreen(val id: Long): Screen
}