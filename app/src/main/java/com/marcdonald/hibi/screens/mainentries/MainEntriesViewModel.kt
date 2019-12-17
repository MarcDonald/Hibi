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
package com.marcdonald.hibi.screens.mainentries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.BookEntryRelation
import com.marcdonald.hibi.data.entity.TagEntryRelation
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.EntryRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.internal.utils.EntryDisplayUtils
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesDisplayItem
import kotlinx.coroutines.launch

class MainEntriesViewModel(private val entryRepository: EntryRepository,
													 private val tagEntryRelationRepository: TagEntryRelationRepository,
													 private val bookEntryRelationRepository: BookEntryRelationRepository,
													 private val entryDisplayUtils: EntryDisplayUtils)
	: ViewModel() {

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayNoResults = MutableLiveData<Boolean>()
	val displayNoResults: LiveData<Boolean>
		get() = _displayNoResults

	private val _entries = MutableLiveData<List<MainEntriesDisplayItem>>()
	val entries: LiveData<List<MainEntriesDisplayItem>>
		get() = _entries

	fun loadEntries() {
		viewModelScope.launch {
			_displayLoading.value = true
			_displayNoResults.value = false
			getMainEntryDisplayItems()
			_displayLoading.value = false
			_displayNoResults.value = entries.value == null || entries.value!!.isEmpty()
		}
	}

	private suspend fun getMainEntryDisplayItems() {
		val allEntries = entryRepository.getAllEntries()
		val tagEntryDisplayItems = tagEntryRelationRepository.getTagEntryDisplayItems()
		val bookEntryDisplayItems = bookEntryRelationRepository.getBookEntryDisplayItems()
		_entries.value = entryDisplayUtils.convertToMainEntriesDisplayItemListWithDateHeaders(allEntries, tagEntryDisplayItems, bookEntryDisplayItems)
	}

	fun setTagsOfSelectedEntries(deleteMode: Boolean, tagIds: List<Int>, entryIds: List<Int>) {
		viewModelScope.launch {
			entryIds.forEach { entryId ->
				tagIds.forEach { tagId ->
					val tagEntryRelation = TagEntryRelation(tagId, entryId)
					if(deleteMode)
						tagEntryRelationRepository.deleteTagEntryRelation(tagEntryRelation)
					else
						tagEntryRelationRepository.addTagEntryRelation(tagEntryRelation)
				}
			}
			getMainEntryDisplayItems()
		}
	}

	fun setBooksOfSelectedEntries(deleteMode: Boolean, bookIds: List<Int>, entryIds: List<Int>) {
		viewModelScope.launch {
			entryIds.forEach { entryId ->
				bookIds.forEach { bookId ->
					val bookEntryRelation = BookEntryRelation(bookId, entryId)
					if(deleteMode)
						bookEntryRelationRepository.deleteBookEntryRelation(bookEntryRelation)
					else
						bookEntryRelationRepository.addBookEntryRelation(bookEntryRelation)
				}
			}
			getMainEntryDisplayItems()
		}
	}

	fun deleteSelectedEntries(idList: List<Int>) {
		viewModelScope.launch {
			idList.forEach { id ->
				entryRepository.deleteEntry(id)
			}
			getMainEntryDisplayItems()
		}
	}

	fun addLocationToSelectedEntries(location: String, idList: List<Int>) {
		viewModelScope.launch {
			idList.forEach { id ->
				entryRepository.saveLocation(id, location)
			}
			getMainEntryDisplayItems()
		}
	}

	fun setSelectedEntriesFavourited(isFavourited: Boolean, idList: List<Int>) {
		viewModelScope.launch {
			idList.forEach { id ->
				entryRepository.setEntryIsFavourite(id, isFavourited)
			}
			getMainEntryDisplayItems()
		}
	}
}