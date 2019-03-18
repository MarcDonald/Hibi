package app.marcdev.hibi.maintabs.mainentries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.internal.lazyDeferred
import app.marcdev.hibi.maintabs.mainentriesrecycler.MainEntriesDisplayItem

class MainEntriesViewModel(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {

  val displayItems by lazyDeferred {
    val allEntries = entryRepository.getAllEntries()
    val allTagEntryRelations = tagEntryRelationRepository.getAllTagEntryRelations()

    // Adds both as sources so that observers get triggered when either are updated
    val result = MediatorLiveData<List<MainEntriesDisplayItem>>()
    result.addSource(allEntries) {
      result.value = combineData(allEntries, allTagEntryRelations)
    }
    result.addSource(allTagEntryRelations) {
      result.value = combineData(allEntries, allTagEntryRelations)
    }

    return@lazyDeferred result
  }

  val entryCount by lazyDeferred {
    return@lazyDeferred entryRepository.getEntryCount()
  }

  private fun combineData(entries: LiveData<List<Entry>>, tagEntryRelations: LiveData<List<TagEntryRelation>>): List<MainEntriesDisplayItem> {
    val itemList = ArrayList<MainEntriesDisplayItem>()

    entries.value?.forEach {
      val item = MainEntriesDisplayItem(it, listOf())
      val listOfTags = ArrayList<TagEntryRelation>()

      if(tagEntryRelations.value != null && tagEntryRelations.value!!.isNotEmpty()) {
        for(x in 0 until tagEntryRelations.value!!.size) {
          if(tagEntryRelations.value!![x].entryId == it.id) {
            listOfTags.add(tagEntryRelations.value!![x])
          }
        }
      }

      item.tagEntryRelations = listOfTags

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