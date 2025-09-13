package com.romit.medreminder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.romit.medreminder.data.local.entities.Medicine
import kotlinx.coroutines.flow.Flow

@Dao
interface MedDao {
    @Query("SELECT * FROM Medicine ORDER BY createdAt DESC")
    fun getListOfAllMedicines(): Flow<List<Medicine>>

    @Upsert
    suspend fun addMedicine(medicine: Medicine): Long

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("SELECT * FROM Medicine WHERE medId = :id")
    suspend fun getMedicineById(id: Long): Medicine?
}