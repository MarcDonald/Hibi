package app.marcdev.hibi.maintabs.calendarfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.maintabs.mainentriesrecycler.MainEntriesDisplayItem
import java.util.*

class CalendarTabViewModel(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {
  var displayItems = MediatorLiveData<List<MainEntriesDisplayItem>>()

  suspend fun updateList(year: Int, month: Int, day: Int) {
    val newEntries = entryRepository.getEntriesOnDate(year, month, day)
    val allTagEntryRelations = tagEntryRelationRepository.getAllTagEntryRelations()

    // Adds both as sources so that observers get triggered when either are updated
    displayItems.addSource(newEntries) {
      displayItems.value = combineData(newEntries, allTagEntryRelations)
    }
    displayItems.addSource(allTagEntryRelations) {
      displayItems.value = combineData(newEntries, allTagEntryRelations)
    }
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