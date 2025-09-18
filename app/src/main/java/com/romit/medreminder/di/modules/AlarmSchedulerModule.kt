package com.romit.medreminder.di.modules

import com.romit.medreminder.notifications.local.NotificationScheduler
import com.romit.medreminder.notifications.local.WorkManagerNotificationScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationSchedulerModule {

    @Binds
    @Singleton
    abstract fun bindNotificationScheduler(
        workManagerScheduler: WorkManagerNotificationScheduler
    ): NotificationScheduler
}