package app.marcdev.nichiroku.addentryscreen

import androidx.lifecycle.ViewModel
import app.marcdev.nichiroku.data.entity.Entry
import app.marcdev.nichiroku.data.repository.EntryRepository

class AddEntryViewModel(private val entryRepository: EntryRepository) : ViewModel() {

  suspend fun addEntry(day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String) {
    entryRepository.addEntry(Entry(day, month, year, hour, minute, content))
  }
}