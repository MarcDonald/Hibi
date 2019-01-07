package app.marcdev.nikki

import android.app.Application
import app.marcdev.nikki.addentryscreen.AddEntryViewModelFactory
import app.marcdev.nikki.data.database.AppDatabase
import app.marcdev.nikki.data.database.DAO
import app.marcdev.nikki.data.database.ProductionAppDatabase
import app.marcdev.nikki.data.network.ConnectivityInterceptor
import app.marcdev.nikki.data.network.ConnectivityInterceptorImpl
import app.marcdev.nikki.data.network.JishoAPIService
import app.marcdev.nikki.data.repository.EntryRepository
import app.marcdev.nikki.data.repository.EntryRepositoryImpl
import app.marcdev.nikki.mainscreen.MainScreenViewModelFactory
import app.marcdev.nikki.searchmoreinfoscreen.SearchMoreInfoViewModelFactory
import app.marcdev.nikki.searchresults.SearchViewModelFactory
import app.marcdev.nikki.viewentryscreen.ViewEntryViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class Nikki : Application(), KodeinAware {

  override val kodein = Kodein.lazy {
    import(androidXModule(this@Nikki))

    bind<AppDatabase>() with singleton { ProductionAppDatabase.invoke(applicationContext) }
    bind<DAO>() with singleton { instance<AppDatabase>().dao() }
    bind<EntryRepository>() with singleton { EntryRepositoryImpl.getInstance(instance()) }
    bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
    bind<JishoAPIService>() with singleton { JishoAPIService(instance()) }
    bind() from provider { MainScreenViewModelFactory(instance()) }
    bind() from provider { AddEntryViewModelFactory(instance()) }
    bind() from provider { ViewEntryViewModelFactory(instance()) }
    bind() from provider { SearchViewModelFactory(instance()) }
    bind() from provider { SearchMoreInfoViewModelFactory() }
  }

  override fun onCreate() {
    super.onCreate()
    if(BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
      Timber.i("Log: Timber Debug Tree planted")
    }
  }
}