package app.marcdev.hibi.maintabs.calendarfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.maintabs.mainentriesrecycler.MainEntriesDisplayItem
import app.marcdev.hibi.maintabs.mainentriesrecycler.TagEntryDisplayItem
import java.util.*

class CalendarTabViewModel(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {
  var displayItems = MediatorLiveData<List<MainEntriesDisplayItem>>()

  suspend fun updateList(year: Int, month: Int, day: Int) {
    val newEntries = entryRepository.getEntriesOnDate(year, month, day)
    val tagEntryDisplayItems = tagEntryRelationRepository.getTagEntryDisplayItems()

    // Adds both as sources so that observers get triggered when either are updated
    displayItems.addSource(newEntries) {
      displayItems.value = combineData(newEntries, tagEntryDisplayItems)
    }
    displayItems.addSource(tagEntryDisplayItems) {
      displayItems.value = combineData(newEntries, tagEntryDisplayItems)
    }
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
    return entriesMutable.sortedWith(
      compareBy(
        { -it.entry.year },
        { -it.entry.month },
        { -it.entry.day },
        { -it.entry.hour },
        { -it.entry.minute },
        { -it.entry.id }
      )
    ).toMutableList()
  }
}