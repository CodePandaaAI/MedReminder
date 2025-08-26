package com.romit.medreminder.data.repository

import com.romit.medreminder.data.local.dao.MedDao
import com.romit.medreminder.data.local.entities.Medicine
import kotlinx.coroutines.flow.Flow

class MedReminderRepository (private val medDao: MedDao) {
    val allMedicines: Flow<List<Medicine>> = medDao.getListOfAllMedicines()

    suspend fun addMedicine(medicine: Medicine) = medDao.addMedicine(medicine)

    suspend fun deleteMedicine(medicine: Medicine) = medDao.deleteMedicine(medicine)
}