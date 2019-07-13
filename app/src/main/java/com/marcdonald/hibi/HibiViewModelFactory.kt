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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.database.AppDatabase
import com.marcdonald.hibi.data.network.jisho.JishoAPIService
import com.marcdonald.hibi.data.repository.*
import com.marcdonald.hibi.internal.utils.FileUtils
import com.marcdonald.hibi.internal.utils.UpdateUtils
import com.marcdonald.hibi.screens.booksscreen.bookentriesfragment.BookEntriesViewModel
import com.marcdonald.hibi.screens.booksscreen.mainbooksfragment.BooksFragmentViewModel
import com.marcdonald.hibi.screens.calendarscreen.CalendarTabViewModel
import com.marcdonald.hibi.screens.entryscreens.viewentryscreen.ViewEntryViewModel
import com.marcdonald.hibi.screens.favouriteentries.FavouriteEntriesViewModel
import com.marcdonald.hibi.screens.mainentries.MainEntriesViewModel
import com.marcdonald.hibi.screens.searchentries.searchentriesscreen.SearchEntriesViewModel
import com.marcdonald.hibi.screens.settings.backupdialog.BackupDialogViewModel
import com.marcdonald.hibi.screens.settings.restoredialog.RestoreDialogViewModel
import com.marcdonald.hibi.screens.settings.updatedialog.UpdateDialogViewModel
import com.marcdonald.hibi.screens.tagsscreen.maintagsfragment.TagsFragmentViewModel
import com.marcdonald.hibi.screens.tagsscreen.taggedentriesfragment.TaggedEntriesViewModel
import com.marcdonald.hibi.screens.throwbackscreen.mainthrowbackscreen.ThrowbackFragmentViewModel
import com.marcdonald.hibi.screens.throwbackscreen.throwbackentriesscreen.ThrowbackEntriesViewModel
import com.marcdonald.hibi.search.searchmoreinfoscreen.SearchMoreInfoViewModel
import com.marcdonald.hibi.search.searchresults.SearchViewModel
import com.marcdonald.hibi.uicomponents.addbookdialog.AddBookViewModel
import com.marcdonald.hibi.uicomponents.addentrytobookdialog.AddEntryToBookViewModel
import com.marcdonald.hibi.uicomponents.addnewworddialog.AddNewWordViewModel
import com.marcdonald.hibi.uicomponents.addtagdialog.AddTagViewModel
import com.marcdonald.hibi.uicomponents.addtagtoentrydialog.AddTagToEntryViewModel
import com.marcdonald.hibi.uicomponents.locationdialog.AddLocationToEntryViewModel
import com.marcdonald.hibi.uicomponents.multiselectdialog.addmultientrytobookdialog.AddMultiEntryToBookViewModel
import com.marcdonald.hibi.uicomponents.multiselectdialog.addtagtomultientrydialog.AddTagToMultiEntryViewModel
import com.marcdonald.hibi.uicomponents.newwordsdialog.NewWordViewModel

@Suppress("UNCHECKED_CAST")
class HibiViewModelFactory(private val entryRepository: EntryRepository,
													 private val tagRepository: TagRepository,
													 private val tagEntryRelationRepository: TagEntryRelationRepository,
													 private val newWordRepository: NewWordRepository,
													 private val bookRepository: BookRepository,
													 private val bookEntryRelationRepository: BookEntryRelationRepository,
													 private val entryImageRepository: EntryImageRepository,
													 private val fileUtils: FileUtils,
													 private val updateUtils: UpdateUtils,
													 private val jishoAPIService: JishoAPIService,
													 private val database: AppDatabase
) : ViewModelProvider.NewInstanceFactory() {

	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return with(modelClass) {
			when {
				isAssignableFrom(MainEntriesViewModel::class.java)         -> MainEntriesViewModel(entryRepository, tagEntryRelationRepository, bookEntryRelationRepository)
				isAssignableFrom(BooksFragmentViewModel::class.java)       -> BooksFragmentViewModel(bookEntryRelationRepository)
				isAssignableFrom(ViewEntryViewModel::class.java)           -> ViewEntryViewModel(entryRepository, tagEntryRelationRepository, newWordRepository, bookEntryRelationRepository, entryImageRepository, fileUtils)
				isAssignableFrom(SearchViewModel::class.java)              -> SearchViewModel(jishoAPIService)
				isAssignableFrom(SearchMoreInfoViewModel::class.java)      -> SearchMoreInfoViewModel()
				isAssignableFrom(AddTagToEntryViewModel::class.java)       -> AddTagToEntryViewModel(tagRepository, tagEntryRelationRepository)
				isAssignableFrom(AddTagViewModel::class.java)              -> AddTagViewModel(tagRepository)
				isAssignableFrom(NewWordViewModel::class.java)             -> NewWordViewModel(newWordRepository)
				isAssignableFrom(AddNewWordViewModel::class.java)          -> AddNewWordViewModel(newWordRepository)
				isAssignableFrom(CalendarTabViewModel::class.java)         -> CalendarTabViewModel(entryRepository, tagEntryRelationRepository, bookEntryRelationRepository)
				isAssignableFrom(TagsFragmentViewModel::class.java)        -> TagsFragmentViewModel(tagEntryRelationRepository)
				isAssignableFrom(TaggedEntriesViewModel::class.java)       -> TaggedEntriesViewModel(tagRepository, tagEntryRelationRepository, bookEntryRelationRepository)
				isAssignableFrom(AddBookViewModel::class.java)             -> AddBookViewModel(bookRepository)
				isAssignableFrom(BookEntriesViewModel::class.java)         -> BookEntriesViewModel(bookRepository, bookEntryRelationRepository, tagEntryRelationRepository)
				isAssignableFrom(AddEntryToBookViewModel::class.java)      -> AddEntryToBookViewModel(bookRepository, bookEntryRelationRepository)
				isAssignableFrom(AddLocationToEntryViewModel::class.java)  -> AddLocationToEntryViewModel(entryRepository)
				isAssignableFrom(SearchEntriesViewModel::class.java)       -> SearchEntriesViewModel(entryRepository, tagRepository, tagEntryRelationRepository, bookRepository, bookEntryRelationRepository)
				isAssignableFrom(AddTagToMultiEntryViewModel::class.java)  -> AddTagToMultiEntryViewModel(tagRepository)
				isAssignableFrom(AddMultiEntryToBookViewModel::class.java) -> AddMultiEntryToBookViewModel(bookRepository)
				isAssignableFrom(BackupDialogViewModel::class.java)        -> BackupDialogViewModel(fileUtils)
				isAssignableFrom(RestoreDialogViewModel::class.java)       -> RestoreDialogViewModel(fileUtils, database)
				isAssignableFrom(UpdateDialogViewModel::class.java)        -> UpdateDialogViewModel(updateUtils)
				isAssignableFrom(ThrowbackFragmentViewModel::class.java)   -> ThrowbackFragmentViewModel(entryRepository, tagEntryRelationRepository, bookEntryRelationRepository)
				isAssignableFrom(ThrowbackEntriesViewModel::class.java)    -> ThrowbackEntriesViewModel(entryRepository, tagEntryRelationRepository, bookEntryRelationRepository)
				isAssignableFrom(FavouriteEntriesViewModel::class.java)    -> FavouriteEntriesViewModel(entryRepository, tagEntryRelationRepository, bookEntryRelationRepository)
				else                                                       -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
			}
		} as T
	}
}