package com.marcdonald.hibi.screens.throwbackscreen.throwbackentriesscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.EntryRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.internal.utils.formatDateForDisplay
import com.marcdonald.hibi.screens.mainentriesrecycler.BookEntryDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.TagEntryDisplayItem
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ThrowbackEntriesViewModel(private val entryRepository: EntryRepository,
																private val tagEntryRelationRepository: TagEntryRelationRepository,
																private val bookEntryRelationRepository: BookEntryRelationRepository)
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
			_toolbarTitle.value = formatDateForDisplay(dateToRetrieve)
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
		_entries.value = combineData(entries, tagEntryDisplayItems, bookEntryDisplayItem)
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