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
package com.marcdonald.hibi.screens.favouriteentries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.EntryRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.internal.utils.EntryDisplayUtils
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesDisplayItem
import kotlinx.coroutines.launch

class FavouriteEntriesViewModel(private val entryRepository: EntryRepository,
																private val tagEntryRelationRepository: TagEntryRelationRepository,
																private val bookEntryRelationRepository: BookEntryRelationRepository,
																private val entryDisplayUtils: EntryDisplayUtils)
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

	fun loadEntries() {
		viewModelScope.launch {
			_displayLoading.value = true
			getMainEntryDisplayItems()
			_displayLoading.value = false
		}
	}

	private suspend fun getMainEntryDisplayItems() {
		val favouriteEntries = entryRepository.getFavouriteEntries()
		val tagEntryDisplayItems = tagEntryRelationRepository.getTagEntryDisplayItems()
		val bookEntryDisplayItem = bookEntryRelationRepository.getBookEntryDisplayItems()
		_entries.value = entryDisplayUtils.convertToMainEntriesDisplayItemListWithDateHeaders(favouriteEntries, tagEntryDisplayItems, bookEntryDisplayItem)
		_displayNoResults.value = entries.value == null || entries.value!!.isEmpty()
	}

	fun removeSelectedItemsFromFavourites(selectedIds: List<Int>) {
		viewModelScope.launch {
			selectedIds.forEach { id ->
				entryRepository.setEntryIsFavourite(id, false)
			}
			getMainEntryDisplayItems()
		}
	}
}