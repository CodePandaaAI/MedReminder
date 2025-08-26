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

@HiltViewModel
class HomeScreenViewModel @Inject constructor(val medReminderRepository: MedReminderRepository): ViewModel() {
    val allMedicines: StateFlow<List<Medicine>> = medReminderRepository.allMedicines.stateIn(
        viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    suspend fun addMedicine(medicine: Medicine) {
        medReminderRepository.addMedicine(medicine)
    }

    suspend fun deleteMedicine(medicine: Medicine) {
        medReminderRepository.deleteMedicine(medicine)
    }
}