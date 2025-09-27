package com.romit.medreminder.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Medicine(
    @PrimaryKey(autoGenerate = true) val medId: Long = 0,
    val name: String,
    val dosage: Int,
    val reminders: String,
    val refillDays: Int,
    val lastRefillDateMillis: Long,
    val notes: String,
    val createdAt: Long = System.currentTimeMillis()
)