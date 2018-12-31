package app.marcdev.nichiroku.addentryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.nichiroku.data.repository.EntryRepository

class AddEntryViewModelFactory(private val entryRepository: EntryRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return AddEntryViewModel(entryRepository) as T
  }
}