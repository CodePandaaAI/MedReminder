package com.romit.medreminder.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.romit.medreminder.data.local.entities.Medicine
import com.romit.medreminder.data.repository.MedReminderRepository
import com.romit.medreminder.notifications.local.NotificationScheduler
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
class AddMedicineScreenViewModel @Inject constructor(
    private val medReminderRepository: MedReminderRepository,
    private val notificationScheduler: NotificationScheduler
) :
    ViewModel() {
    private val _medicineUiState = MutableStateFlow(MedicineUiState())

    val medicineUiState: StateFlow<MedicineUiState> = _medicineUiState.asStateFlow()

    suspend fun addMedicine(): Boolean {
        return try {
            val dosageAmount: Int = when (medicineUiState.value.dosage) {
                DosageType.None -> 0
                DosageType.OnceDaily -> 1
                DosageType.TwiceDaily -> 2
                DosageType.Custom -> medicineUiState.value.customDosage
            }
            val reminders = medicineUiState.value.reminders.joinToString(",")
            val medicine = Medicine(
                name = medicineUiState.value.medName,
                dosage = dosageAmount,
                reminders = reminders,
                refillDays = medicineUiState.value.refillDays,
                lastRefillDateMillis = System.currentTimeMillis(),
                notes = medicineUiState.value.notes
            )
            val newMedId = medReminderRepository.upsertMedicine(medicine)

            val newMedicine = medicine.copy(medId = newMedId)

            notificationScheduler.scheduleReminders(newMedicine)
            true
        } catch (e: Exception) {
            false // Simple boolean return
        }

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

    fun changeCustomMedDosage(customDosage: Int) {
        val validDosage = customDosage.coerceIn(3, 10) // Simple range limit
        _medicineUiState.update { medicineUiState ->
            medicineUiState.copy(customDosage = validDosage)
        }
    }

    // Current - no bounds checking
    fun removeReminder(index: Int) {
        val currentReminders = medicineUiState.value.reminders
        if (index in currentReminders.indices) { // Simple bounds check
            val newList = currentReminders.toMutableList()
            newList.removeAt(index)
            _medicineUiState.update { medicineUiState ->
                medicineUiState.copy(reminders = newList.toList())
            }
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
        val refillDaysValid = if (refillDays.isNotBlank() && refillDays.toIntOrNull() != null) {
            val days = refillDays.toInt()
            if (days in 1..365) days else 0 // Simple range check
        } else 0

        _medicineUiState.update { medicineUiState ->
            medicineUiState.copy(refillDays = refillDaysValid)
        }
    }

    fun addMedicineNote(note: String) {
        _medicineUiState.update { medicineUiState ->
            medicineUiState.copy(notes = note)
        }
    }


    fun convertTo12HourFormat(time: String): String {
        return try {
            val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
            val displayFormatter = DateTimeFormatter.ofPattern("hh:mm a")
            localTime.format(displayFormatter)
        } catch (e: Exception) {
            time // Return original if parsing fails
        }
    }
}