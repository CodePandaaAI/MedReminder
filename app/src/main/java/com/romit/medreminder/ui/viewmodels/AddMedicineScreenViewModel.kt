package com.romit.medreminder.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.romit.medreminder.data.local.entities.Medicine
import com.romit.medreminder.data.repository.MedReminderRepository
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

@HiltViewModel
class AddMedicineScreenViewModel @Inject constructor(private val medReminderRepository: MedReminderRepository) :
    ViewModel() {
    private val _medicineUiState = MutableStateFlow(MedicineUiState())

    val medicineUiState: StateFlow<MedicineUiState> = _medicineUiState.asStateFlow()

    suspend fun addMedicine() {
        val dosageAmount: Int = when (medicineUiState.value.dosage) {
            DosageType.None -> 0
            DosageType.OnceDaily -> 1
            DosageType.TwiceDaily -> 2
            DosageType.Custom -> medicineUiState.value.customDosage.toInt()
        }

        val reminders = medicineUiState.value.reminders.joinToString(",")
        val medicine = Medicine(
            name = _medicineUiState.value.medName,
            dosage = dosageAmount,
            reminders = reminders,
            refillDays = medicineUiState.value.refillDays,
            notes = medicineUiState.value.notes
        )
        medReminderRepository.addMedicine(medicine)

    }

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
        val storagePattern = DateTimeFormatter.ofPattern("HH:mm")
        val newTime = time.format(storagePattern)

        if (!medicineUiState.value.reminders.contains(newTime)) {
            val newReminderList = medicineUiState.value.reminders + newTime
            _medicineUiState.update { medicineUiState ->
                medicineUiState.copy(reminders = newReminderList)
            }
        }
    }

    fun validateAndChangeMedicineRefillDays(refillDays: String) {
        val refillDaysValid =
            if (refillDays.isNotBlank() && refillDays.toIntOrNull() != null) refillDays.toInt()
            else 0
        if (refillDaysValid >= 0) {
            _medicineUiState.update { medicineUiState ->
                medicineUiState.copy(refillDays = refillDaysValid)
            }
        }
    }

    fun addMedicineNote(note: String) {
        _medicineUiState.update { medicineUiState ->
            medicineUiState.copy(notes = note)
        }
    }


    fun convertTo12HourFormat(time: String): String {
        val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        val displayFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        return localTime.format(displayFormatter)
    }
}