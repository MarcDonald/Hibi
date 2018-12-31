package app.marcdev.nichiroku

import android.app.Application
import app.marcdev.nichiroku.addentryscreen.AddEntryViewModelFactory
import app.marcdev.nichiroku.data.database.AppDatabase
import app.marcdev.nichiroku.data.database.DAO
import app.marcdev.nichiroku.data.database.ProductionAppDatabase
import app.marcdev.nichiroku.data.repository.EntryRepository
import app.marcdev.nichiroku.data.repository.EntryRepositoryImpl
import app.marcdev.nichiroku.mainscreen.MainScreenViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class Nichiroku : Application(), KodeinAware {

  override val kodein = Kodein.lazy {
    bind<AppDatabase>() with singleton { ProductionAppDatabase.invoke(applicationContext) }
    bind<DAO>() with singleton { instance<AppDatabase>().dao() }
    bind<EntryRepository>() with singleton { EntryRepositoryImpl.getInstance(instance()) }
    bind() from provider { MainScreenViewModelFactory(instance()) }
    bind() from provider { AddEntryViewModelFactory(instance()) }
  }

  override fun onCreate() {
    super.onCreate()
    if(BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
      Timber.i("Log: Timber Debug Tree planted")
    }
  }
}