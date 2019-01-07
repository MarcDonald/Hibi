package app.marcdev.hibi.viewentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.repository.EntryRepository

class ViewEntryViewModel(private val entryRepository: EntryRepository) : ViewModel() {

  suspend fun getEntry(id: Int): LiveData<Entry> {
    return entryRepository.getEntry(id)
  }

  suspend fun deleteEntry(id: Int) {
    return entryRepository.deleteEntry(id)
  }
}