package com.romit.medreminder.ui.viewmodels

import android.R.attr.name
import androidx.lifecycle.ViewModel
import com.romit.medreminder.ui.DosageType
import com.romit.medreminder.ui.MedicineUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddMedicineScreenViewModel : ViewModel() {
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
}