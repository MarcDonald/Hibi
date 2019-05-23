package app.marcdev.hibi.maintabs.searchentries.searchentriesscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.maintabs.mainentriesrecycler.MainEntriesDisplayItem
import app.marcdev.hibi.maintabs.mainentriesrecycler.TagEntryDisplayItem
import app.marcdev.hibi.maintabs.searchentries.EntrySearchCriteria
import kotlinx.coroutines.launch

class SearchEntriesViewModel(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {

  private val _entries = MutableLiveData<List<MainEntriesDisplayItem>>()
  val entries: LiveData<List<MainEntriesDisplayItem>>
    get() = _entries

  private val _displayLoading = MutableLiveData<Boolean>()
  val displayLoading: LiveData<Boolean>
    get() = _displayLoading

  private val _displayNoResults = MutableLiveData<Boolean>()
  val displayNoResults: LiveData<Boolean>
    get() = _displayNoResults

  fun loadAllEntries() {
    viewModelScope.launch {
      _displayLoading.value = true
      _displayNoResults.value = false
      getMainEntryDisplayItems()
      _displayLoading.value = false
      _displayNoResults.value = entries.value == null || entries.value!!.isEmpty()
    }
  }

  fun onCriteriaChange(searchCriteria: EntrySearchCriteria) {
    viewModelScope.launch {
      _displayLoading.value = true
      _displayNoResults.value = false
      val filteredResults = filterEntries(searchCriteria)
      getMainEntryDisplayItems(filteredResults)
      _displayLoading.value = false
      _displayNoResults.value = entries.value == null || entries.value!!.isEmpty()
    }
  }

  private suspend fun filterEntries(searchCriteria: EntrySearchCriteria): List<Entry> {
    val allEntries = entryRepository.getAllEntries()
    val filteredByDate = filterByDate(allEntries, searchCriteria)
    val filteredByContent = filterByContent(filteredByDate, searchCriteria)
    val filteredByLocation = filterByLocation(filteredByContent, searchCriteria)
    return filteredByLocation
  }

  private fun filterByDate(entries: List<Entry>, searchCriteria: EntrySearchCriteria): List<Entry> {
    val returnList = mutableListOf<Entry>()
    val completeStartDate = getCompleteDate(searchCriteria.startDay, searchCriteria.startMonth, searchCriteria.startYear)
    val completeEndDate = getCompleteDate(searchCriteria.endDay, searchCriteria.endMonth, searchCriteria.endYear)

    for(entry in entries) {
      val entryCompleteDate = getCompleteDate(entry.day, entry.month, entry.year)
      if(entryCompleteDate in completeStartDate..completeEndDate)
        returnList.add(entry)
    }
    return returnList
  }

  private fun filterByContent(entries: List<Entry>, searchCriteria: EntrySearchCriteria): List<Entry> {
    val returnList = mutableListOf<Entry>()

    if(searchCriteria.content.isNotBlank()) {
      for(entry in entries) {
        if(entry.content.contains(searchCriteria.content, true))
          returnList.add(entry)
      }
    } else {
      return entries
    }

    return returnList
  }

  private fun filterByLocation(entries: List<Entry>, searchCriteria: EntrySearchCriteria): List<Entry> {
    val returnList = mutableListOf<Entry>()

    if(searchCriteria.location.isNotBlank()) {
      for(entry in entries) {
        if(entry.location.contains(searchCriteria.location, true))
          returnList.add(entry)
      }
    } else {
      return entries
    }

    return returnList
  }

  private fun getCompleteDate(day: Int, month: Int, year: Int): Int {
    val dayTwoDigit = if(day < 10)
      "0$day"
    else {
      "$day"
    }

    val monthTwoDigit = if(month < 10)
      "0$month"
    else {
      "$month"
    }

    return "$year$monthTwoDigit$dayTwoDigit".toInt()
  }

  private suspend fun getMainEntryDisplayItems(entries: List<Entry>) {
    val tagEntryDisplayItems = tagEntryRelationRepository.getTagEntryDisplayItems()
    _entries.value = combineData(entries, tagEntryDisplayItems)
  }

  private suspend fun getMainEntryDisplayItems() {
    val entries = entryRepository.getAllEntries()
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
}