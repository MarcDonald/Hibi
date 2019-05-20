package app.marcdev.hibi.maintabs.booksfragment.bookentriesfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.BookRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.maintabs.mainentriesrecycler.MainEntriesDisplayItem
import app.marcdev.hibi.maintabs.mainentriesrecycler.TagEntryDisplayItem
import kotlinx.coroutines.launch

class BookEntriesViewModel(private val bookRepository: BookRepository, private val bookEntryRelationRepository: BookEntryRelationRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {

  private var _bookId = 0
  val bookId: Int
    get() = _bookId

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

  fun passArguments(bookIdArg: Int) {
    _bookId = bookIdArg
    getBookName()
  }

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
    val entries = bookEntryRelationRepository.getEntriesWithBook(bookId)
    val tagEntryDisplayItems = tagEntryRelationRepository.getTagEntryDisplayItems()
    _entries.value = combineData(entries, tagEntryDisplayItems)
  }

  private fun combineData(entries: List<Entry>, tagEntryDisplayItems: List<TagEntryDisplayItem>): List<MainEntriesDisplayItem> {
    val itemList = ArrayList<MainEntriesDisplayItem>()

    entries.forEach { entry ->
      val item = MainEntriesDisplayItem(entry, listOf())
      val listOfTags = ArrayList<String>()

      tagEntryDisplayItems.forEach { tagEntryDisplayItem ->
        if(tagEntryDisplayItem.entryId == entry.id) {
          listOfTags.add(tagEntryDisplayItem.tagName)
        }
      }

      item.tags = listOfTags
      itemList.add(item)
    }

    return addListHeaders(itemList)
  }

  private fun addListHeaders(allItems: MutableList<MainEntriesDisplayItem>): List<MainEntriesDisplayItem> {
    val listWithHeaders = mutableListOf<MainEntriesDisplayItem>()
    listWithHeaders.addAll(allItems)

    var lastMonth = 12
    var lastYear = 9999
    if(allItems.isNotEmpty()) {
      lastMonth = allItems.first().entry.month + 1
      lastYear = allItems.first().entry.year
    }

    val headersToAdd = mutableListOf<Pair<Int, MainEntriesDisplayItem>>()

    for(x in 0 until allItems.size) {
      if(((allItems[x].entry.month < lastMonth) && (allItems[x].entry.year == lastYear))
         || (allItems[x].entry.month > lastMonth) && (allItems[x].entry.year < lastYear)
         || (allItems[x].entry.year < lastYear)
      ) {
        val header = Entry(0, allItems[x].entry.month, allItems[x].entry.year, 0, 0, "")
        val headerItem = MainEntriesDisplayItem(header, listOf())
        lastMonth = allItems[x].entry.month
        lastYear = allItems[x].entry.year
        headersToAdd.add(Pair(x, headerItem))
      }
    }

    for((add, x) in (0 until headersToAdd.size).withIndex()) {
      listWithHeaders.add(headersToAdd[x].first + add, headersToAdd[x].second)
    }

    return listWithHeaders
  }

  private fun getBookName() {
    viewModelScope.launch {
      _toolbarTitle.value = bookRepository.getBookName(bookId)
    }
  }
}