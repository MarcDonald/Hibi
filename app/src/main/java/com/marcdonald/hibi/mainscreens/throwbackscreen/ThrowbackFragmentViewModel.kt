package com.marcdonald.hibi.mainscreens.throwbackscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.EntryRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.mainscreens.mainentriesrecycler.BookEntryDisplayItem
import com.marcdonald.hibi.mainscreens.mainentriesrecycler.MainEntriesDisplayItem
import com.marcdonald.hibi.mainscreens.mainentriesrecycler.TagEntryDisplayItem
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

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

  private val _entries = MutableLiveData<List<MainEntriesDisplayItem>>()
  val entries: LiveData<List<MainEntriesDisplayItem>>
    get() = _entries

  fun loadEntries() {
    viewModelScope.launch {
      _displayLoading.value = true
      _displayNoResults.value = false
      getDisplayItems()
      _displayLoading.value = false
      _displayNoResults.value = entries.value == null || entries.value!!.isEmpty()
    }
  }

  private suspend fun getDisplayItems() {
    val lastMonthEntries = getMonthThrowbackEntries()
    val lastYearEntries = getYearThrowbackEntries()
    val allEntries = lastMonthEntries + lastYearEntries
    val tagEntryDisplayItems = tagEntryRelationRepository.getTagEntryDisplayItems()
    val bookEntryDisplayItems = bookEntryRelationRepository.getBookEntryDisplayItems()
    _entries.value = combineData(allEntries, tagEntryDisplayItems, bookEntryDisplayItems)
  }

  private suspend fun getMonthThrowbackEntries(): List<Entry> {
    val returnList: List<Entry>
    val februaryMaxDay = if(today.getActualMaximum(Calendar.DAY_OF_YEAR) > 365) 29 else 28

    returnList = if(today.get(Calendar.MONTH) == 0) {
      getLastDecemberEntries()
    } else if(today.get(Calendar.MONTH) == 2 && today.get(Calendar.DAY_OF_MONTH) > februaryMaxDay) {
      listOf()
    } else if(today.get(Calendar.DAY_OF_MONTH) == 31) {
      if(doesLastMonthHave30Days()) {
        listOf()
      } else {
        getEntriesOnThisDateLastMonth()
      }
    } else {
      getEntriesOnThisDateLastMonth()
    }

    return returnList
  }

  private suspend fun getYearThrowbackEntries(): List<Entry> {
    val returnList = mutableListOf<Entry>()
    val dateToRetrieve = today.clone() as Calendar
    entryRepository.getAllYears().forEach { year ->
      if(year != today.get(Calendar.YEAR)) {
        if(isDateOnPreviousYearValid(year)) {
          dateToRetrieve.set(Calendar.YEAR, year)
          val entriesOnDate = entryRepository.getEntriesOnDate(dateToRetrieve)
          returnList.addAll(entriesOnDate)
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

  private suspend fun getLastDecemberEntries(): List<Entry> {
    val dateToRetrieve = today.clone() as Calendar
    dateToRetrieve.set(Calendar.MONTH, 11)
    dateToRetrieve.set(Calendar.YEAR, today.get(Calendar.YEAR) - 1)
    return entryRepository.getEntriesOnDate(dateToRetrieve)
  }

  private suspend fun getEntriesOnThisDateLastMonth(): List<Entry> {
    val dateToRetrieve = today.clone() as Calendar
    dateToRetrieve.set(Calendar.MONTH, today.get(Calendar.MONTH) - 1)
    return entryRepository.getEntriesOnDate(dateToRetrieve)
  }

  private fun doesLastMonthHave30Days(): Boolean {
    val lastMonth = today.clone() as Calendar
    // Need to set day to 1 as if it's 31 and the last month has 30 days it doesn't behave as expected
    lastMonth.set(Calendar.DAY_OF_MONTH, 1)
    lastMonth.set(Calendar.MONTH, lastMonth.get(Calendar.MONTH - 1))
    return lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH) == 30
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