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
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@HiltViewModel
class HomeScreenViewModel @Inject constructor(medReminderRepository: MedReminderRepository): ViewModel() {
    val allMedicines: StateFlow<List<Medicine>> = medReminderRepository.allMedicines.stateIn(
        viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

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