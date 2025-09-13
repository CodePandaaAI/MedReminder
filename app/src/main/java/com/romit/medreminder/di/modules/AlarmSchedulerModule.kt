package com.romit.medreminder.di.modules

import com.romit.medreminder.notifications.local.AlarmScheduler
import com.romit.medreminder.notifications.local.AlarmSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmSchedulerModule {
    @Binds
    @Singleton
    abstract fun bindAlarmScheduler(alarmScheduler: AlarmSchedulerImpl): AlarmScheduler
}