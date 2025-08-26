package com.romit.medreminder.di.modules

import android.content.Context
import com.romit.medreminder.data.local.dao.MedDao
import com.romit.medreminder.data.local.database.AppDatabase
import com.romit.medreminder.data.repository.MedReminderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MedicineModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    @Provides
    fun providesDao(database: AppDatabase): MedDao {
        return database.medDao()
    }
    @Provides
    @Singleton
    fun providesMedicineRepository(medDao: MedDao): MedReminderRepository {
        return MedReminderRepository(medDao)
    }
}