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
import com.marcdonald.hibi.screens.booksscreen.bookentriesfragment.BookEntriesViewModelFactory
import com.marcdonald.hibi.screens.booksscreen.mainbooksfragment.BooksFragmentViewModelFactory
import com.marcdonald.hibi.screens.calendarscreen.CalendarTabViewModelFactory
import com.marcdonald.hibi.screens.entryscreens.addentryscreen.AddEntryViewModelFactory
import com.marcdonald.hibi.screens.entryscreens.viewentryscreen.ViewEntryViewModelFactory
import com.marcdonald.hibi.screens.mainentries.MainEntriesViewModelFactory
import com.marcdonald.hibi.screens.mainscreen.MainScreenViewModelFactory
import com.marcdonald.hibi.screens.searchentries.searchentriesscreen.SearchEntriesViewModelFactory
import com.marcdonald.hibi.screens.settings.backupdialog.BackupDialogViewModelFactory
import com.marcdonald.hibi.screens.settings.restoredialog.RestoreDialogViewModelFactory
import com.marcdonald.hibi.screens.settings.updatedialog.UpdateDialogViewModelFactory
import com.marcdonald.hibi.screens.tagsscreen.maintagsfragment.TagsFragmentViewModelFactory
import com.marcdonald.hibi.screens.tagsscreen.taggedentriesfragment.TaggedEntriesViewModelFactory
import com.marcdonald.hibi.screens.throwbackscreen.mainthrowbackscreen.ThrowbackFragmentViewModelFactory
import com.marcdonald.hibi.screens.throwbackscreen.throwbackentriesscreen.ThrowbackEntriesViewModelFactory
import com.marcdonald.hibi.search.searchmoreinfoscreen.SearchMoreInfoViewModelFactory
import com.marcdonald.hibi.search.searchresults.SearchViewModelFactory
import com.marcdonald.hibi.uicomponents.addbookdialog.AddBookViewModelFactory
import com.marcdonald.hibi.uicomponents.addentrytobookdialog.AddEntryToBookViewModelFactory
import com.marcdonald.hibi.uicomponents.addnewworddialog.AddNewWordViewModelFactory
import com.marcdonald.hibi.uicomponents.addtagdialog.AddTagViewModelFactory
import com.marcdonald.hibi.uicomponents.addtagtoentrydialog.AddTagToEntryViewModelFactory
import com.marcdonald.hibi.uicomponents.locationdialog.AddLocationToEntryViewModelFactory
import com.marcdonald.hibi.uicomponents.multiselectdialog.addmultientrytobookdialog.AddMultiEntryToBookViewModelFactory
import com.marcdonald.hibi.uicomponents.multiselectdialog.addtagtomultientrydialog.AddTagToMultiEntryViewModelFactory
import com.marcdonald.hibi.uicomponents.newwordsdialog.NewWordViewModelFactory
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
		// </editor-fold>
		// <editor-fold desc="Connectivity and Jisho API">
		bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
		bind<GithubStatusCodeInterceptor>() with singleton { GithubStatusCodeInterceptorImpl() }
		bind<JishoAPIService>() with singleton { JishoAPIService(instance()) }
		bind<GithubAPIService>() with singleton { GithubAPIService(instance(), instance()) }
		// </editor-fold>
		// <editor-fold desc="View models">
		bind() from provider { MainEntriesViewModelFactory(instance(), instance(), instance()) }
		bind() from provider { MainScreenViewModelFactory(instance(), instance()) }
		bind() from provider { AddEntryViewModelFactory(instance(), instance(), instance(), instance(), instance(), instance(), instance()) }
		bind() from provider { ViewEntryViewModelFactory(instance(), instance(), instance(), instance(), instance(), instance()) }
		bind() from provider { SearchViewModelFactory(instance()) }
		bind() from provider { SearchMoreInfoViewModelFactory() }
		bind() from provider { AddTagToEntryViewModelFactory(instance(), instance()) }
		bind() from provider { AddTagViewModelFactory(instance()) }
		bind() from provider { NewWordViewModelFactory(instance()) }
		bind() from provider { AddNewWordViewModelFactory(instance()) }
		bind() from provider { CalendarTabViewModelFactory(instance(), instance(), instance()) }
		bind() from provider { TagsFragmentViewModelFactory(instance()) }
		bind() from provider { TaggedEntriesViewModelFactory(instance(), instance(), instance()) }
		bind() from provider { AddBookViewModelFactory(instance()) }
		bind() from provider { BooksFragmentViewModelFactory(instance()) }
		bind() from provider { BookEntriesViewModelFactory(instance(), instance(), instance()) }
		bind() from provider { AddEntryToBookViewModelFactory(instance(), instance()) }
		bind() from provider { AddLocationToEntryViewModelFactory(instance()) }
		bind() from provider { SearchEntriesViewModelFactory(instance(), instance(), instance(), instance(), instance()) }
		bind() from provider { AddTagToMultiEntryViewModelFactory(instance()) }
		bind() from provider { AddMultiEntryToBookViewModelFactory(instance()) }
		bind() from provider { BackupDialogViewModelFactory(instance()) }
		bind() from provider { RestoreDialogViewModelFactory(instance(), instance()) }
		bind() from provider { UpdateDialogViewModelFactory(instance()) }
		bind() from provider { ThrowbackFragmentViewModelFactory(instance(), instance(), instance()) }
		bind() from provider { ThrowbackEntriesViewModelFactory(instance(), instance(), instance()) }
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