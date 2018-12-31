package app.marcdev.nichiroku.addentryscreen

import androidx.lifecycle.ViewModel
import app.marcdev.nichiroku.data.entity.Entry
import app.marcdev.nichiroku.data.repository.EntryRepository

class AddEntryViewModel(private val entryRepository: EntryRepository) : ViewModel() {

  suspend fun addEntry(date: String, time: String, content: String) {
    entryRepository.addEntry(Entry(date, time, content))
  }
}