package com.romit.medreminder.ui.utils

import com.romit.medreminder.ui.DosageType
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.time.format.DateTimeFormatter

interface SharedViewModelFunctions {
    fun changeMedName(name: String)
    fun changeMedDosage(medDosage: DosageType)
    fun changeCustomMedDosage(customDosage: Int)
    // Current - no bounds checking
    fun removeReminder(index: Int)

    fun validateAndAddReminder(hour: Int, minute: Int)
    fun validateAndChangeMedicineRefillDays(refillDays: String)

    fun addMedicineNote(note: String)

    fun convertTo12HourFormat(time: String): String
}