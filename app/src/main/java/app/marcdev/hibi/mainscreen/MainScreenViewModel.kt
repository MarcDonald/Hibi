package app.marcdev.hibi.mainscreen

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.internal.lazyDeferred

class MainScreenViewModel(private val entryRepository: EntryRepository) : ViewModel() {

  val allEntries by lazyDeferred {
    entryRepository.getAllEntries()
  }

  fun sortEntries(entries: List<Entry>): List<Entry> {
    val entriesMutable = entries.toMutableList()
    val sortedEntries = entriesMutable.sortedWith(
      compareBy(
        { -it.year },
        { -it.month },
        { -it.day },
        { -it.hour },
        { -it.minute },
        { -it.id!! }
      )
    ).toMutableList()

    return addListHeaders(sortedEntries)
  }

  private fun addListHeaders(allItems: MutableList<Entry>): List<Entry> {
    val listWithHeaders = mutableListOf<Entry>()
    listWithHeaders.addAll(allItems)

    var lastMonth = 12
    var lastYear = 9999
    if(allItems.isNotEmpty()) {
      lastMonth = allItems.first().month + 1
      lastYear = allItems.first().year
    }

    val headersToAdd = mutableListOf<Pair<Int, Entry>>()

    for(x in 0 until allItems.size) {
      if(((allItems[x].month < lastMonth) && (allItems[x].year == lastYear))
         || (allItems[x].month > lastMonth) && (allItems[x].year < lastYear)
         || (allItems[x].year < lastYear)
      ) {
        val header = Entry(0, allItems[x].month, allItems[x].year, 0, 0, "")
        lastMonth = allItems[x].month
        lastYear = allItems[x].year
        headersToAdd.add(Pair(x, header))
      }
    }

    for((add, x) in (0 until headersToAdd.size).withIndex()) {
      listWithHeaders.add(headersToAdd[x].first + add, headersToAdd[x].second)
    }

    return listWithHeaders
  }
}