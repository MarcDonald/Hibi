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
package com.marcdonald.hibi.screens.calendarscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.EntryRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.screens.mainentriesrecycler.BookEntryDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.TagEntryDisplayItem
import kotlinx.coroutines.launch
import java.util.*

class CalendarTabViewModel(private val entryRepository: EntryRepository,
													 private val tagEntryRelationRepository: TagEntryRelationRepository,
													 private val bookEntryRelationRepository: BookEntryRelationRepository)
	: ViewModel() {

	private val _entries = MutableLiveData<List<MainEntriesDisplayItem>>()
	val entries: LiveData<List<MainEntriesDisplayItem>>
		get() = _entries

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayNoResults = MutableLiveData<Boolean>()
	val displayNoResults: LiveData<Boolean>
		get() = _displayNoResults

	fun loadData() {
		val calendar = Calendar.getInstance()
		loadEntriesForDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
	}

	fun loadEntriesForDate(year: Int, month: Int, day: Int) {
		viewModelScope.launch {
			_displayLoading.value = true
			_displayNoResults.value = false
			getMainEntryDisplayItems(year, month, day)
			_displayLoading.value = false
			_displayNoResults.value = entries.value == null || entries.value!!.isEmpty()
		}
	}

	private suspend fun getMainEntryDisplayItems(year: Int, month: Int, day: Int) {
		val allEntries = entryRepository.getEntriesOnDate(year, month, day)
		val tagEntryDisplayItems = tagEntryRelationRepository.getTagEntryDisplayItems()
		val bookEntryDisplayItems = bookEntryRelationRepository.getBookEntryDisplayItems()
		_entries.value = combineData(allEntries, tagEntryDisplayItems, bookEntryDisplayItems)
	}

	private fun combineData(entries: List<Entry>, tagEntryDisplayItems: List<TagEntryDisplayItem>, bookEntryDisplayItems: List<BookEntryDisplayItem>): List<MainEntriesDisplayItem> {
		val itemList = ArrayList<MainEntriesDisplayItem>()

		entries.forEach { entry ->
			val item = MainEntriesDisplayItem(entry, listOf(), listOf())
			val listOfTags = ArrayList<String>()
			val listOfBooks = ArrayList<String>()

			tagEntryDisplayItems.forEach { tagEntryDisplayItem ->
				if(tagEntryDisplayItem.entryId == entry.id) {
					listOfTags.add(tagEntryDisplayItem.tagName)
				}
			}

			bookEntryDisplayItems.forEach { bookEntryDisplayItem ->
				if(bookEntryDisplayItem.entryId == entry.id) {
					listOfBooks.add(bookEntryDisplayItem.bookName)
				}
			}

			item.tags = listOfTags
			item.books = listOfBooks
			itemList.add(item)
		}

		return itemList
	}
}