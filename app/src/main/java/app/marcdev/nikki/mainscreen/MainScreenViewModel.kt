package app.marcdev.nikki.mainscreen

import androidx.lifecycle.ViewModel
import app.marcdev.nikki.data.entity.Entry
import app.marcdev.nikki.data.repository.EntryRepository
import app.marcdev.nikki.internal.lazyDeferred

class MainScreenViewModel(private val entryRepository: EntryRepository) : ViewModel() {

  val allEntries by lazyDeferred {
    entryRepository.getAllEntries()
  }

  fun sortEntries(entries: List<Entry>): List<Entry> {
    val entriesMutable = entries.toMutableList()
    return entriesMutable.sortedWith(
      compareBy(
        { -it.year },
        { -it.month },
        { -it.day },
        { -it.hour },
        { -it.minute },
        { -it.id!! }
      )
    )
  }
}