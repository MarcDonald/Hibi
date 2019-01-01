package app.marcdev.nikki.addentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.marcdev.nikki.data.entity.Entry
import app.marcdev.nikki.data.repository.EntryRepository

class AddEntryViewModel(private val entryRepository: EntryRepository) : ViewModel() {

  suspend fun addEntry(day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String) {
    entryRepository.addEntry(Entry(day, month, year, hour, minute, content))
  }

  suspend fun getEntry(id: Int): LiveData<Entry> {
    return entryRepository.getEntry(id)
  }

  suspend fun updateEntry(day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String, entryId: Int) {
    val entryToUpdate = Entry(day, month, year, hour, minute, content)
    entryToUpdate.id = entryId
    entryRepository.addEntry(entryToUpdate)
  }
}