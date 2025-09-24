package com.romit.medreminder.notifications.local

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.romit.medreminder.data.local.entities.Medicine
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface NotificationScheduler {
    fun scheduleReminders(medicine: Medicine)
    fun cancelReminders(medicine: Medicine)
}


class WorkManagerNotificationScheduler @Inject constructor(@param:ApplicationContext private val context: Context) : NotificationScheduler {

    override fun scheduleReminders(medicine: Medicine) {
        val reminders = medicine.reminders.split(",").filter { it.isNotBlank() }

        reminders.forEachIndexed { index, timeString ->
            scheduleReminder(medicine, index, timeString.trim())
        }
    }

    override fun cancelReminders(medicine: Medicine) {
        val workManager = WorkManager.getInstance(context)

        // Cancel all work for this medicine
        val reminders = medicine.reminders.split(",").filter { it.isNotBlank() }
        reminders.forEachIndexed { index, _ ->
            val workName = "medicine_${medicine.medId}_reminder_$index"
            workManager.cancelUniqueWork(workName)
        }
    }

    private fun scheduleReminder(medicine: Medicine, reminderIndex: Int, timeString: String) {
        val time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"))
        val delayMinutes = calculateDelayUntilTime(time)

        val inputData = Data.Builder()
            .putString("MEDICINE_NAME", medicine.name)
            .putString("DOSAGE_INFO", "Time for your ${medicine.name}")
            .putInt("MEDICINE_ID", medicine.medId.toInt())
            .putLong("ACTUAL_MEDICINE_ID", medicine.medId)
            .putInt("REMINDER_INDEX", reminderIndex)
            .putString("REMINDER_TIME", timeString)
            .putString("ALL_REMINDERS", medicine.reminders)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<MedicineReminderWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .setInputData(inputData)
            .addTag("medicine_reminders")
            .build()

        val workName = "medicine_${medicine.medId}_reminder_$reminderIndex"

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    private fun calculateDelayUntilTime(targetTime: LocalTime): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetTime.hour)
            set(Calendar.MINUTE, targetTime.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If time has passed today, schedule for tomorrow
            if (timeInMillis <= now.timeInMillis) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        return (target.timeInMillis - now.timeInMillis) / (1000 * 60) // Convert to minutes
    }
}