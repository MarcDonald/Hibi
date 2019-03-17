package app.marcdev.hibi

import android.os.Bundle
import app.marcdev.hibi.internal.base.HibiActivity
import timber.log.Timber

class MainActivity : HibiActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.v("Log: onCreate: Started")
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
