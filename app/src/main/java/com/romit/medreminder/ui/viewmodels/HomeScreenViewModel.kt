package com.romit.medreminder.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romit.medreminder.data.local.entities.Medicine
import com.romit.medreminder.data.repository.MedReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val medReminderRepository: MedReminderRepository) :
    ViewModel() {
    val allMedicines: StateFlow<List<Medicine>> = medReminderRepository.allMedicines.stateIn(
        viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList()
    )

    fun convertTo12HourFormat(time: String): String {
        return try {
            val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
            val displayFormatter = DateTimeFormatter.ofPattern("hh:mm a")
            localTime.format(displayFormatter)
        } catch (e: Exception) {
            time // Return original if parsing fails
        }
    }

    // This function calculates how many days are left until the next refill.
    fun calculateRefillDaysRemaining(medicine: Medicine): Int {
        // A. We get the last refill date from our database.
        val lastRefillDate = Calendar.getInstance().apply {
            timeInMillis = medicine.lastRefillDateMillis
        }

        // B. We get today's date.
        val today = Calendar.getInstance()

        // C. We calculate the number of days that have passed since the last refill.
        val daysPassed = TimeUnit.DAYS.convert(
            today.timeInMillis - lastRefillDate.timeInMillis,
            TimeUnit.MILLISECONDS
        ).toInt()

        // D. The days remaining is the total frequency minus the days that have passed.
        val daysRemaining = medicine.refillDays - daysPassed

        // The result might be negative if they're overdue for a refill.
        // We can show 0 if they have already passed the date.
        return if (daysRemaining >= 0) daysRemaining else 0
    }

    // In your ViewModel or Repository
    fun onRefillButtonClicked(medicine: Medicine) {
        // We update the medicine entity to set the new refill date.
        viewModelScope.launch {
            val updatedMedicine = medicine.copy(
                lastRefillDateMillis = System.currentTimeMillis(),
            )
            medReminderRepository.upsertMedicine(updatedMedicine)
        }

    }
}