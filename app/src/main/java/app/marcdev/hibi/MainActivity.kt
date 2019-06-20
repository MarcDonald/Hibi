package app.marcdev.hibi

import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import app.marcdev.hibi.internal.ADD_ENTRY_NOTIFICATION_INTENT_ACTION
import app.marcdev.hibi.internal.ADD_ENTRY_SHORTCUT_INTENT_ACTION
import app.marcdev.hibi.internal.REMINDER_NOTIFICATION_ID
import app.marcdev.hibi.internal.base.HibiActivity
import app.marcdev.hibi.mainscreens.MainScreenFragmentDirections
import timber.log.Timber

class MainActivity : HibiActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.v("Log: onCreate: Started")
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if(intent.action == ADD_ENTRY_SHORTCUT_INTENT_ACTION || intent.action == ADD_ENTRY_NOTIFICATION_INTENT_ACTION) {
      Timber.d("Log: onCreate: Started with Add Entry intent")

      val addEntryAction = MainScreenFragmentDirections.addEntryAction()
      Navigation.findNavController(this, R.id.nav_host_fragment).navigate(addEntryAction)

      if(intent.action == ADD_ENTRY_NOTIFICATION_INTENT_ACTION)
        NotificationManagerCompat.from(this).cancel(REMINDER_NOTIFICATION_ID)
    }
  }
}
