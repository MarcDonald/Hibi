package app.marcdev.hibi

import android.os.Bundle
import androidx.navigation.Navigation
import app.marcdev.hibi.internal.base.HibiActivity
import app.marcdev.hibi.maintabs.MainScreenFragmentDirections
import timber.log.Timber

class MainActivity : HibiActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.v("Log: onCreate: Started")
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if(intent.action == "app.marcdev.hibi.intent.ADD_ENTRY") {
      Timber.d("Log: onCreate: Started with Add Entry intent")

      val addEntryAction = MainScreenFragmentDirections.addEntryAction()
      Navigation.findNavController(this, R.id.nav_host_fragment).navigate(addEntryAction)
    }
  }
}
