package com.romit.medreminder.notifications.local

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.romit.medreminder.MainActivity
import com.romit.medreminder.R

fun medicationNotification(
    context: Context,
    notificationId: Int,
    medicineName: String,
    dosageInfo: String
) {
    val channelId = "default_channel_id" // Must match the channel created in MyApp

    val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val mainPendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.medication) // You'll need a suitable icon
        .setContentTitle("Medication Reminder: $medicineName")
        .setContentText(dosageInfo)
        .setPriority(NotificationCompat.PRIORITY_HIGH) // High priority to make it a heads-up notification
        .setCategory(NotificationCompat.CATEGORY_REMINDER)
        .setContentIntent(mainPendingIntent)
        .setAutoCancel(true)

    val notificationManager = NotificationManagerCompat.from(context)
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        notificationManager.notify(notificationId, builder.build())
    }
}