package com.romit.medreminder.notifications.local

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MedicineReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        return try {
            val medicineName = inputData.getString("MEDICINE_NAME") ?: "Medicine Reminder"
            val dosageInfo = inputData.getString("DOSAGE_INFO") ?: "Time for your medication"
            val medicineId = inputData.getInt("MEDICINE_ID", 0)

            // Show notification
            createMedicationReminder(applicationContext, medicineId, medicineName, dosageInfo)

            // Schedule next day's reminder
            scheduleNextDayReminder()

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun scheduleNextDayReminder() {
        val reminderTime = inputData.getString("REMINDER_TIME") ?: return
        val medicineId = inputData.getLong("ACTUAL_MEDICINE_ID", -1L)
        val reminderIndex = inputData.getInt("REMINDER_INDEX", -1)

        if (medicineId == -1L || reminderIndex == -1) return

        // Calculate delay until same time tomorrow
        val time = LocalTime.parse(reminderTime, DateTimeFormatter.ofPattern("HH:mm"))
        val delayMinutes = calculateDelayUntilTomorrow(time)

        val nextDayWorkRequest = OneTimeWorkRequestBuilder<MedicineReminderWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .setInputData(inputData) // Reuse same input data
            .addTag("medicine_reminders")
            .build()

        val workName = "medicine_${medicineId}_reminder_$reminderIndex"

        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.REPLACE,
                nextDayWorkRequest
            )
    }

    private fun calculateDelayUntilTomorrow(targetTime: LocalTime): Long {
        val now = Calendar.getInstance()
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, targetTime.hour)
            set(Calendar.MINUTE, targetTime.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return (tomorrow.timeInMillis - now.timeInMillis) / (1000 * 60) // Convert to minutes
    }
}