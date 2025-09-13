package com.romit.medreminder.notifications.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val medicineName = intent.getStringExtra("MEDICINE_NAME") ?: "Medicine Reminder"
        val dosageInfo = intent.getStringExtra("DOSAGE_INFO") ?: "Time for your medication."
        // Use the medicine ID as the notification ID to allow updating/cancellation if needed
        val notificationId = intent.getIntExtra("MEDICINE_ID", 0)

        medicationNotification(context, notificationId, medicineName, dosageInfo)
    }
}