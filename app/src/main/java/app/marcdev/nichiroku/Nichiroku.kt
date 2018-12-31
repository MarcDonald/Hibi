package app.marcdev.nichiroku

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import timber.log.Timber

class Nichiroku : Application(), KodeinAware {

  override val kodein = Kodein.lazy {
    // TODO
  }

  override fun onCreate() {
    super.onCreate()
    if(BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
      Timber.i("Log: Timber Debug Tree planted")
    }
  }
}