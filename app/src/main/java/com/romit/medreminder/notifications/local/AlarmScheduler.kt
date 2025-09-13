package com.romit.medreminder.notifications.local

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.romit.medreminder.data.local.entities.Medicine
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

interface AlarmScheduler {
    fun schedule(medicine: Medicine)
    fun cancel(medicine: Medicine)
}

class AlarmSchedulerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(medicine: Medicine) {
        val reminders = medicine.reminders.split(",")

        reminders.forEachIndexed { index, timeString ->
            if (timeString.isNotBlank()) {
                val time = LocalTime.parse(timeString.trim(), DateTimeFormatter.ofPattern("HH:mm"))

                val intent = Intent(context, NotificationReceiver::class.java).apply {
                    putExtra("MEDICINE_NAME", medicine.name)
                    putExtra("DOSAGE_INFO", "Time for your ${medicine.name}")
                    putExtra("MEDICINE_ID", medicine.medId.toInt())
                }

                // Use unique request code to avoid conflicts
                val requestCode = "${medicine.medId}_$index".hashCode()

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, time.hour)
                    set(Calendar.MINUTE, time.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)

                    // If time has passed today, schedule for tomorrow
                    if (timeInMillis <= System.currentTimeMillis()) {
                        add(Calendar.DAY_OF_MONTH, 1)
                    }
                }

                // Use setExactAndAllowWhileIdle for better reliability
                try {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )

                } catch (e: SecurityException) {
                    // Fallback if exact alarms are not allowed
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }
        }
    }

    override fun cancel(medicine: Medicine) {
        val reminders = medicine.reminders.split(",")

        reminders.forEachIndexed { index, _ ->
            val intent = Intent(context, NotificationReceiver::class.java)

            // Use the same request code as in schedule()
            val requestCode = "${medicine.medId}_$index".hashCode()

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}