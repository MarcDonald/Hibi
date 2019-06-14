package app.marcdev.hibi.internal.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class AlertReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    val helper = NotificationHelper()
    if(context != null) {
      helper.sendReminderNotification(context)
    } else {
      Timber.e("Log: onReceive: Context is null")
    }
  }
}