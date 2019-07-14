/*
 * Copyright 2019 Marc Donald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marcdonald.hibi

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.marcdonald.hibi.data.database.AppDatabase
import com.marcdonald.hibi.data.database.DAO
import com.marcdonald.hibi.data.database.ProductionAppDatabase
import com.marcdonald.hibi.data.network.ConnectivityInterceptor
import com.marcdonald.hibi.data.network.ConnectivityInterceptorImpl
import com.marcdonald.hibi.data.network.github.GithubAPIService
import com.marcdonald.hibi.data.network.github.GithubStatusCodeInterceptor
import com.marcdonald.hibi.data.network.github.GithubStatusCodeInterceptorImpl
import com.marcdonald.hibi.data.network.jisho.JishoAPIService
import com.marcdonald.hibi.data.repository.*
import com.marcdonald.hibi.internal.NOTIFICATION_CHANNEL_REMINDER_ID
import com.marcdonald.hibi.internal.utils.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class Hibi : Application(), KodeinAware {

	override val kodein = Kodein.lazy {
		import(androidXModule(this@Hibi))

		// <editor-fold desc="Database">
		bind<AppDatabase>() with singleton { ProductionAppDatabase.invoke(applicationContext) }
		bind<DAO>() with singleton { instance<AppDatabase>().dao() }
		bind<EntryRepository>() with singleton { EntryRepositoryImpl.getInstance(instance(), instance()) }
		bind<TagRepository>() with singleton { TagRepositoryImpl.getInstance(instance()) }
		bind<TagEntryRelationRepository>() with singleton { TagEntryRelationRepositoryImpl.getInstance(instance()) }
		bind<NewWordRepository>() with singleton { NewWordRepositoryImpl.getInstance(instance()) }
		bind<BookRepository>() with singleton { BookRepositoryImpl.getInstance(instance()) }
		bind<BookEntryRelationRepository>() with singleton { BookEntryRelationRepositoryImpl.getInstance(instance()) }
		bind<EntryImageRepository>() with singleton { EntryImageRepositoryImpl.getInstance(instance()) }
		// </editor-fold>
		// <editor-fold desc="Utils">
		bind<FileUtils>() with provider { FileUtilsImpl(instance()) }
		bind<ThemeUtils>() with provider { ThemeUtilsImpl(instance()) }
		bind<UpdateUtils>() with provider { UpdateUtilsImpl(instance()) }
		bind<EntryDisplayUtils>() with provider { EntryDisplayUtilsImpl(instance()) }
		bind<DateTimeUtils>() with provider { DateTimeUtilsImpl(instance()) }
		// </editor-fold>
		// <editor-fold desc="Connectivity and Jisho API">
		bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
		bind<GithubStatusCodeInterceptor>() with singleton { GithubStatusCodeInterceptorImpl() }
		bind<JishoAPIService>() with singleton { JishoAPIService(instance()) }
		bind<GithubAPIService>() with singleton { GithubAPIService(instance(), instance()) }
		// </editor-fold>
		// <editor-fold desc="View Model Factories">
		bind() from provider { HibiViewModelFactory(instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance()) }
		bind() from provider { HibiAndroidViewModelFactory(instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance()) }
		// </editor-fold>
	}

	override fun onCreate() {
		super.onCreate()
		if(BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
			Timber.i("Log: Timber Debug Tree planted")
		}
		createNotificationChannels()
	}

	private fun createNotificationChannels() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val reminderChannel = NotificationChannel(
				NOTIFICATION_CHANNEL_REMINDER_ID,
				resources.getString(R.string.reminder_notification_channel_title),
				NotificationManager.IMPORTANCE_DEFAULT)
			reminderChannel.description = resources.getString(R.string.reminder_notification_channel_description)

			val manager: NotificationManager = getSystemService(NotificationManager::class.java)
			manager.createNotificationChannel(reminderChannel)
		}
	}
}