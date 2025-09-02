package com.romit.medreminder.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.romit.medreminder.ui.DosageType
import com.romit.medreminder.ui.MedicineUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@HiltViewModel
class AddMedicineScreenViewModel @Inject constructor() : ViewModel() {
    private val _medicineUiState = MutableStateFlow(MedicineUiState())

    val medicineUiState: StateFlow<MedicineUiState> = _medicineUiState.asStateFlow()

    fun changeMedName(name: String) {
        _medicineUiState.update { medicineUiState ->
            medicineUiState.copy(medName = name)
        }
    }

    fun changeMedDosage(medDosage: DosageType) {
        _medicineUiState.update { medicineUiState ->
            medicineUiState.copy(dosage = medDosage)
        }
    }

    fun changeCustomMedDosage(customDosage: Float) {
        _medicineUiState.update { medicineUiState ->
            medicineUiState.copy(customDosage = customDosage)
        }
    }

    fun removeReminder(index: Int) {
        val newList = medicineUiState.value.reminders.toMutableList()
        newList.removeAt(index)
        _medicineUiState.update { medicineUiState ->
            medicineUiState.copy(reminders = newList.toList())
        }
    }

    fun validateAndAddReminder(hour: Int, minute: Int) {
        val time = LocalTime.of(hour, minute)
        val formatedPattern = DateTimeFormatter.ofPattern("hh:mm a")
        val newTime = time.format(formatedPattern)
        val newReminderList = medicineUiState.value.reminders + newTime
        if(!medicineUiState.value.reminders.contains(newTime)){
            _medicineUiState.update { medicineUiState ->
                medicineUiState.copy(reminders = newReminderList)
            }
        }
    }
}