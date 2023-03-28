package com.chathurangashan.backgroundtasks.broadcastreceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.chathurangashan.backgroundtasks.R

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Log.d(BootReceiver::class.simpleName,intent.action.toString())

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {

            val sharedPreferences = context
                .getSharedPreferences("com.chathurangashan.backgroundtasks",Context.MODE_PRIVATE)

            if(sharedPreferences.contains(context.getString(R.string.alarm_manger_reminder_time))){

                val alarmMilliseconds = sharedPreferences.getLong(
                    context.getString(R.string.alarm_manger_reminder_time),
                    0L
                )

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val broadcastIntent = Intent(context, AlarmManagerTrigger::class.java)
                val pending = PendingIntent.getBroadcast(
                    context,
                    0,
                    broadcastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmMilliseconds, pending)

            }
        }
    }
}