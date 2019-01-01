package app.marcdev.nikki

import android.app.Application
import app.marcdev.nikki.addentryscreen.AddEntryViewModelFactory
import app.marcdev.nikki.data.database.AppDatabase
import app.marcdev.nikki.data.database.DAO
import app.marcdev.nikki.data.database.ProductionAppDatabase
import app.marcdev.nikki.data.repository.EntryRepository
import app.marcdev.nikki.data.repository.EntryRepositoryImpl
import app.marcdev.nikki.mainscreen.MainScreenViewModelFactory
import app.marcdev.nikki.viewentryscreen.ViewEntryViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class Nikki : Application(), KodeinAware {

  override val kodein = Kodein.lazy {
    bind<AppDatabase>() with singleton { ProductionAppDatabase.invoke(applicationContext) }
    bind<DAO>() with singleton { instance<AppDatabase>().dao() }
    bind<EntryRepository>() with singleton { EntryRepositoryImpl.getInstance(instance()) }
    bind() from provider { MainScreenViewModelFactory(instance()) }
    bind() from provider { AddEntryViewModelFactory(instance()) }
    bind() from provider { ViewEntryViewModelFactory(instance()) }
  }

  override fun onCreate() {
    super.onCreate()
    if(BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
      Timber.i("Log: Timber Debug Tree planted")
    }
  }
}