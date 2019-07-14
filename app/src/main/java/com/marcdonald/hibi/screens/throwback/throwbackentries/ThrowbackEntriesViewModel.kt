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
package com.marcdonald.hibi.screens.throwback.throwbackentries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.EntryRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.internal.utils.DateTimeUtils
import com.marcdonald.hibi.internal.utils.EntryDisplayUtils
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesDisplayItem
import kotlinx.coroutines.launch
import java.util.*

class ThrowbackEntriesViewModel(private val entryRepository: EntryRepository,
																private val tagEntryRelationRepository: TagEntryRelationRepository,
																private val bookEntryRelationRepository: BookEntryRelationRepository,
																private val entryDisplayUtils: EntryDisplayUtils,
																private val dateTimeUtils: DateTimeUtils)
	: ViewModel() {

	private val dateToRetrieve = Calendar.getInstance()

	private val _toolbarTitle = MutableLiveData<String>()
	val toolbarTitle: LiveData<String>
		get() = _toolbarTitle

	private val _entries = MutableLiveData<List<MainEntriesDisplayItem>>()
	val entries: LiveData<List<MainEntriesDisplayItem>>
		get() = _entries

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayNoResults = MutableLiveData<Boolean>()
	val displayNoResults: LiveData<Boolean>
		get() = _displayNoResults

	fun passArguments(day: Int, month: Int, year: Int) {
		dateToRetrieve.set(Calendar.YEAR, year)
		dateToRetrieve.set(Calendar.MONTH, month)
		dateToRetrieve.set(Calendar.DAY_OF_MONTH, day)

		viewModelScope.launch {
			_toolbarTitle.value = dateTimeUtils.formatDateForDisplay(dateToRetrieve)
		}
	}

	fun loadEntries() {
		viewModelScope.launch {
			_displayLoading.value = true
			getMainEntryDisplayItems()
			_displayLoading.value = false
		}
	}

	private suspend fun getMainEntryDisplayItems() {
		val entries = entryRepository.getEntriesOnDate(dateToRetrieve, true)
		val tagEntryDisplayItems = tagEntryRelationRepository.getTagEntryDisplayItems()
		val bookEntryDisplayItem = bookEntryRelationRepository.getBookEntryDisplayItems()
		_entries.value = entryDisplayUtils.convertToMainEntriesDisplayItemList(entries, tagEntryDisplayItems, bookEntryDisplayItem)
	}
}