package com.marcdonald.hibi.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.marcdonald.hibi.MainActivity
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.*

class ReminderAlertReceiver : BroadcastReceiver() {

	fun startAlarm(context: Context) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, ReminderAlertReceiver::class.java)
		val pendingIntent = PendingIntent.getBroadcast(context, REMINDER_ALERT_RECEIVER_REQUEST_CODE, intent, 0)

		val timeInMillis = PreferenceManager.getDefaultSharedPreferences(context).getLong(PREF_REMINDER_TIME, 0)

		if(timeInMillis != 0L)
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
	}

	fun cancelAlarm(context: Context) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, ReminderAlertReceiver::class.java)
		val pendingIntent = PendingIntent.getBroadcast(context, REMINDER_ALERT_RECEIVER_REQUEST_CODE, intent, 0)

		alarmManager.cancel(pendingIntent)
	}

	override fun onReceive(context: Context?, intent: Intent?) {
		context?.let {
			val addIntent = Intent().apply {
				action = ADD_ENTRY_NOTIFICATION_INTENT_ACTION
				setPackage(PACKAGE)
				setClass(context, MainActivity::class.java)
			}
			val pendingIntent = PendingIntent.getActivity(context, REMINDER_NOTIFICATION_REQUEST_CODE, addIntent, 0)

			val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_REMINDER_ID)
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setCategory(NotificationCompat.CATEGORY_REMINDER)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_reminder_notification)
				.setContentTitle(context.resources.getString(R.string.reminder_notification_title))
				.setContentText(context.resources.getString(R.string.reminder_notification_description))
				.addAction(R.drawable.ic_add_24dp, context.resources.getString(R.string.reminder_notification_add_action), pendingIntent)
				.setContentIntent(pendingIntent)
			builder.color = context.resources.getColor(R.color.lightThemeColorAccent, null)
			val notification = builder.build()

			val manager = NotificationManagerCompat.from(context)
			manager.notify(REMINDER_NOTIFICATION_ID, notification)
		}
	}
}