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
package com.marcdonald.hibi.screens.throwback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.EntryRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesDisplayItem
import kotlinx.coroutines.launch
import java.util.*

class ThrowbackFragmentViewModel(private val entryRepository: EntryRepository,
																 private val tagEntryRelationRepository: TagEntryRelationRepository,
																 private val bookEntryRelationRepository: BookEntryRelationRepository)
	: ViewModel() {

	private val today = Calendar.getInstance()

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayNoResults = MutableLiveData<Boolean>()
	val displayNoResults: LiveData<Boolean>
		get() = _displayNoResults

	private val _displayItems = MutableLiveData<List<ThrowbackDisplayItem>>()
	val displayItems: LiveData<List<ThrowbackDisplayItem>>
		get() = _displayItems

	fun loadEntries() {
		viewModelScope.launch {
			_displayLoading.value = true
			_displayNoResults.value = false
			getDisplayItems()
			_displayLoading.value = false
			_displayNoResults.value = displayItems.value == null || displayItems.value!!.isEmpty()
		}
	}

	private suspend fun getDisplayItems() {
		val lastMonthThrowback = getMonthThrowback()
		val lastYearThrowbacks = getYearThrowbacks()

		val allItems = if(lastMonthThrowback != null)
			listOf(lastMonthThrowback) + lastYearThrowbacks
		else
			lastYearThrowbacks

		_displayItems.value = allItems
	}

	private suspend fun getMonthThrowback(): ThrowbackDisplayItem? {
		val returnItem: ThrowbackDisplayItem?
		val februaryMaxDay = if(today.getActualMaximum(Calendar.DAY_OF_YEAR) > 365) 29 else 28

		returnItem = if(today.get(Calendar.MONTH) == 0) {
			getLastDecemberThrowbackItem()
		} else if(today.get(Calendar.MONTH) == 2 && today.get(Calendar.DAY_OF_MONTH) > februaryMaxDay) {
			null
		} else if(today.get(Calendar.DAY_OF_MONTH) == 31) {
			if(doesLastMonthHave30Days()) {
				null
			} else {
				getThrowbackItemOnThisDateLastMonth()
			}
		} else {
			getThrowbackItemOnThisDateLastMonth()
		}

		return returnItem
	}

	private suspend fun getYearThrowbacks(): List<ThrowbackDisplayItem> {
		val returnList = mutableListOf<ThrowbackDisplayItem>()

		val dateToRetrieve = today.clone() as Calendar
		entryRepository.getAllYears().forEach { year ->
			if(year != today.get(Calendar.YEAR)) {
				if(isDateOnPreviousYearValid(year)) {
					dateToRetrieve.set(Calendar.YEAR, year)
					val throwbackItem = getThrowbackDisplayItem(dateToRetrieve)
					if(throwbackItem != null)
						returnList.add(throwbackItem)
				}
			}
		}
		return returnList
	}

	private fun isDateOnPreviousYearValid(year: Int): Boolean {
		val calendarToCheck = today.clone() as Calendar
		// Need to set day to 1 as if it's 31 and the last month has 30 days it doesn't behave as expected
		if(calendarToCheck.get(Calendar.MONTH) == 1 && calendarToCheck.get(Calendar.DAY_OF_MONTH) == 29) {
			calendarToCheck.set(Calendar.DAY_OF_MONTH, 1)
			calendarToCheck.set(Calendar.YEAR, year)
			if(calendarToCheck.getActualMaximum(Calendar.DAY_OF_YEAR) == 365) {
				return false
			}
		}
		return true
	}

	private suspend fun getLastDecemberThrowbackItem(): ThrowbackDisplayItem? {
		val dateToRetrieve = today.clone() as Calendar
		dateToRetrieve.set(Calendar.MONTH, 11)
		dateToRetrieve.set(Calendar.YEAR, today.get(Calendar.YEAR) - 1)
		return getThrowbackDisplayItem(dateToRetrieve)
	}

	private suspend fun getThrowbackItemOnThisDateLastMonth(): ThrowbackDisplayItem? {
		val dateToRetrieve = today.clone() as Calendar
		dateToRetrieve.set(Calendar.MONTH, today.get(Calendar.MONTH) - 1)
		return getThrowbackDisplayItem(dateToRetrieve)
	}

	private fun doesLastMonthHave30Days(): Boolean {
		val lastMonth = today.clone() as Calendar
		// Need to set day to 1 as if it's 31 and the last month has 30 days it doesn't behave as expected
		lastMonth.set(Calendar.DAY_OF_MONTH, 1)
		lastMonth.set(Calendar.MONTH, lastMonth.get(Calendar.MONTH) - 1)
		return lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH) == 30
	}

	private suspend fun getThrowbackDisplayItem(dateToRetrieve: Calendar): ThrowbackDisplayItem? {
		val entry = entryRepository.getFirstEntryOnDate(dateToRetrieve)
		return if(entry != null) {
			val tagsToAdd = mutableListOf<String>()
			val allTags = tagEntryRelationRepository.getTagsWithEntry(entry.id)
			allTags.forEach { tag ->
				tagsToAdd.add(tag.name)
			}

			val booksToAdd = mutableListOf<String>()
			val allBooks = bookEntryRelationRepository.getBooksWithEntry(entry.id)
			allBooks.forEach { book ->
				booksToAdd.add(book.name)
			}

			val mainEntryDisplayItem = MainEntriesDisplayItem(entry, tagsToAdd, booksToAdd)
			val amountOfOtherEntries = entryRepository.getAmountOfEntriesOnDate(dateToRetrieve) - 1
			ThrowbackDisplayItem(mainEntryDisplayItem, amountOfOtherEntries)
		} else {
			null
		}
	}
}