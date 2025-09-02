package com.romit.medreminder.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.romit.medreminder.data.local.dao.MedDao
import com.romit.medreminder.data.local.entities.Medicine

@Database(entities = [Medicine::class], exportSchema = false, version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medDao(): MedDao

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "medicine_database"
                            ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}