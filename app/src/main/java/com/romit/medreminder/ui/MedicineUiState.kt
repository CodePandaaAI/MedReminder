package com.romit.medreminder.ui

import androidx.compose.runtime.Stable

@Stable
data class MedicineUiState(
    val medName: String = "",
    val dosage: DosageType = DosageType.None,
    val reminders: List<String> = emptyList(),
    val refillDays: Int = 0,
    val notes: String? = null,
    val customDosage: Float = 0f,
)

@Stable
enum class DosageType {
    None, OnceDaily, TwiceDaily, Custom
}