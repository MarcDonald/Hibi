package app.marcdev.hibi.mainscreens.calendarfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.mainscreens.mainentriesrecycler.BookEntryDisplayItem
import app.marcdev.hibi.mainscreens.mainentriesrecycler.MainEntriesDisplayItem
import app.marcdev.hibi.mainscreens.mainentriesrecycler.TagEntryDisplayItem
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