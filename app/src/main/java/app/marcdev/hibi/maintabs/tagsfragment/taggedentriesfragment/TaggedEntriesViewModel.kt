package app.marcdev.hibi.maintabs.tagsfragment.taggedentriesfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.data.repository.TagRepository
import app.marcdev.hibi.maintabs.mainentriesrecycler.MainEntriesDisplayItem
import app.marcdev.hibi.maintabs.mainentriesrecycler.TagEntryDisplayItem

class TaggedEntriesViewModel(private val tagRepository: TagRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {

  var displayItems = MediatorLiveData<List<MainEntriesDisplayItem>>()

  suspend fun updateList(tagId: Int) {
    val newEntries = tagEntryRelationRepository.getEntriesWithTag(tagId)
    val tagEntryDisplayItems = tagEntryRelationRepository.getTagEntryDisplayItems()

    // Adds both as sources so that observers get triggered when either are updated
    displayItems.addSource(newEntries) {
      displayItems.value = combineData(newEntries, tagEntryDisplayItems)
    }
    displayItems.addSource(tagEntryDisplayItems) {
      displayItems.value = combineData(newEntries, tagEntryDisplayItems)
    }
  }

  suspend fun getTagName(tagId: Int): String {
    return tagRepository.getTagName(tagId)
  }

  private fun combineData(entries: LiveData<List<Entry>>, tagEntryDisplayItems: LiveData<List<TagEntryDisplayItem>>): List<MainEntriesDisplayItem> {
    val itemList = ArrayList<MainEntriesDisplayItem>()

    entries.value?.forEach {
      val item = MainEntriesDisplayItem(it, listOf())
      val listOfTags = ArrayList<String>()

      if(tagEntryDisplayItems.value != null && tagEntryDisplayItems.value!!.isNotEmpty()) {
        for(x in 0 until tagEntryDisplayItems.value!!.size) {
          if(tagEntryDisplayItems.value!![x].entryId == it.id) {
            listOfTags.add(tagEntryDisplayItems.value!![x].tagName)
          }
        }
      }

      item.tags = listOfTags

      itemList.add(item)
    }

    return sortEntries(itemList)
  }

  private fun sortEntries(items: List<MainEntriesDisplayItem>): List<MainEntriesDisplayItem> {
    val entriesMutable = items.toMutableList()
    val sortedEntries = entriesMutable.sortedWith(
      compareBy(
        { -it.entry.year },
        { -it.entry.month },
        { -it.entry.day },
        { -it.entry.hour },
        { -it.entry.minute },
        { -it.entry.id }
      )
    ).toMutableList()

    return addListHeaders(sortedEntries)
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