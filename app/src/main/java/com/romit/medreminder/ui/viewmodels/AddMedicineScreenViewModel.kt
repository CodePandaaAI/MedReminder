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
import java.util.Locale

@HiltViewModel
class AddMedicineScreenViewModel @Inject constructor() : ViewModel() {
    private val _medicineUiState = MutableStateFlow(MedicineUiState())

    val medicineUiState: StateFlow<MedicineUiState> = _medicineUiState.asStateFlow()

//    fun addNameAndDosage(name: String, dosage: DosageType) {
//        _medicineUiState.update { medicineUiState ->
//            medicineUiState.copy(
//                medName = name,
//                dosage = dosage
//            )
//        }
//    }

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
        val newTime = String.format(Locale.getDefault(),"%02d:%02d", hour, minute)
        val newList = medicineUiState.value.reminders + newTime
        if (!medicineUiState.value.reminders.contains(newTime)) {
            _medicineUiState.update { medUiState ->
                medUiState.copy(reminders = newList)

            }
        }
    }

    fun convertTo12HourFormat(time: String): String {
        val (hourStr, minuteStr) = time.split(":")
        val hour = hourStr.toInt()

        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        val amPm = if (hour < 12) "AM" else "PM"

        return String.format(Locale.getDefault(),"%d:%s %s",displayHour, minuteStr, amPm)
    }
}