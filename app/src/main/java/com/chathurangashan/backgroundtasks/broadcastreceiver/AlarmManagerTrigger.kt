package com.chathurangashan.backgroundtasks.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.chathurangashan.backgroundtasks.R

private const val ALARM_MANAGER_NOTIFICATION_ID = 5
private const val CHANNEL_ID = "alarm_manager_channel"
class AlarmManagerTrigger: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val sharedPreferences = context
            .getSharedPreferences("com.chathurangashan.backgroundtasks",Context.MODE_PRIVATE)

        val notificationManager = getSystemService(context, NotificationManager::class.java)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getText(R.string.medicine_reminder_notification_title))
            .setContentText(context.getText(R.string.medicine_reminder_notification_description))
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Alarm manager notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "Alarm manager notification"
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        notificationManager?.notify(ALARM_MANAGER_NOTIFICATION_ID, notification)

        with (sharedPreferences.edit()) {
            remove(context.getString(R.string.alarm_manger_reminder_time))
            apply()
        }
    }

}